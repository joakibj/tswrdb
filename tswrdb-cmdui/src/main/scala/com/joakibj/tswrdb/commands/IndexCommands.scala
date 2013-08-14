/*
 * tswrdb - a program and API to export the TSW resource database
 *
 * Copyright (C) 2013 Joakim Bjørnstad <joakibj@gmail.com>
 *
 * Licensed under the GNU General Public License version 2.
 * Please see the LICENSE file for the license text in verbatim.
 */

package com.joakibj.tswrdb.commands

import com.joakibj.tswrdb.rdb.index.RdbIndexFileReader
import java.io.File
import com.joakibj.tswrdb.rdb.RdbTypes
import com.joakibj.tswrdb.Config
import com.joakibj.tswrdb.rdb.util.ByteUtils

object IndexCommands {
  val indexInfo = new ShowIndexInfoCommand
  val indexSize = new ShowIndexSizeCommand

  class ShowIndexInfoCommand extends Command with ByteUtils {
    def execute(config: Config) {
      val reader = RdbIndexFileReader(new File(config.tswDirectory, "RDB/le.idx"))
      val header = reader.indexHeader
      reader.close()

      println("Version: " + header.version)
      println("Hash: " + toHex(header.hash))
      println("Number of entries: " + header.numEntries)
    }
  }

  class ShowIndexSizeCommand extends Command {
    def execute(config: Config) {
      val reader = RdbIndexFileReader(new File(config.tswDirectory, "RDB/le.idx"))
      val tbl = reader.getIndexTable
      tbl.types.foreach {
        x =>
          val rt = RdbTypes.find(x).get
          println(rt + ": size: " + tbl.entriesForType(x).size)
      }
      reader.close()
    }
  }

}

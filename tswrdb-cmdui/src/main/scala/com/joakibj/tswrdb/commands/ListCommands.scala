/*
 * tswrdb - a program and API to export the TSW resource database
 *
 * Copyright (C) 2013 Joakim Bjørnstad <joakibj@gmail.com>
 *
 * Licensed under the GNU General Public License version 2.
 * Please see the LICENSE file for the license text in verbatim.
 */

package com.joakibj.tswrdb.commands

import com.joakibj.tswrdb.Config
import com.joakibj.tswrdb.rdb.{RdbType, RdbTypes}
import com.joakibj.tswrdb.rdb.index.RdbIndexFileReader
import java.io.File

object ListCommands {
  val all = new ListAllCommand
  val wellUnderstood = new ListWellUnderstoodCommand

  trait ListCommand extends ExitCommands {
    def repeat(str: String)(times: Int) = str * times

    def listRdbTypes(tswDirectory: File, rdbTypes: List[RdbType]) {
      val blanks = repeat(" ") _
      val dashes = repeat("-") _

      val reader = RdbIndexFileReader(new File(tswDirectory, "RDB/le.idx"))
      val tbl = reader.getIndexTable

      val maxNameWidth = rdbTypes.map(_.name.length).max
      val columnLine = "\tRdbType \tName" + blanks(maxNameWidth - 4) + " \tNumber of entries"
      println(columnLine)
      println("\t" + dashes(columnLine.length + 3))

      rdbTypes foreach {
        (rdbType) =>
          val numEntries = tbl.entriesForType(rdbType.id).size
          val width = maxNameWidth - rdbType.name.length
          println(f"\t${rdbType.id}%d \t${rdbType.name}%s" + blanks(width) + f"\t$numEntries%d")

      }
      exit()
    }
  }

  class ListAllCommand extends Command with ListCommand {
    def execute(config: Config) {
      println("Printing all RdbTypes.")
      listRdbTypes(config.tswDirectory, RdbTypes.values)
    }
  }

  class ListWellUnderstoodCommand extends Command with ListCommand {
    def execute(config: Config) {
      println("Printing well understood RdbTypes.")
      listRdbTypes(config.tswDirectory, RdbTypes.values.filter(_.understood))
    }
  }

}

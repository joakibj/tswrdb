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
import java.io.{FileInputStream, File}
import com.joakibj.tswrdb.rdb.strings.{StringRdbDataTransformer, RdbStringFileReader}
import com.joakibj.tswrdb.rdb.export.RdbExporter
import com.joakibj.tswrdb.rdb.RdbTypes

object StringCommands {
  val default = new StringExportCommand

  class StringExportCommand extends Command with ExitCommands {
    def execute(config: Config) {
      RdbExporter(new File(config.tswDirectory, "RDB"), new StringRdbDataTransformer).exportAll(RdbTypes.strings)
    }
  }
}

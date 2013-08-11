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
import com.joakibj.tswrdb.rdb.{RdbTypeNotFoundException, RdbTypes}
import com.joakibj.tswrdb.rdb.export.RdbExporter
import java.io.{FileInputStream, File}
import com.joakibj.tswrdb.rdb.strings.RdbStringFileReader

object ExportCommands {
  val default = new DefaultExportCommand

  class DefaultExportCommand extends Command with ExitCommands {
    def execute(config: Config) {
      try {
        val rdbType = RdbTypes.find(config.rdbType).getOrElse {
          throw new RdbTypeNotFoundException("RdbType: " + config.rdbType + " was not found")
        }
        println("Exporting RdbType: " + rdbType + " into exported/" + rdbType + " ...")
        RdbExporter(new File(config.tswDirectory, "RDB")).exportAll(rdbType)
      } catch {
        case ex: RdbTypeNotFoundException => exit(ex.getMessage, 1)
        case ex: RuntimeException => exit(ex.getMessage, 1)
      }
    }
  }
}


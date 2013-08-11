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
import java.io.File
import com.joakibj.tswrdb.rdb.strings.{RdbStringDataExporter, StringLanguage, RdbStringLanguageIndexReader, StringRdbDataXmlTransformer}
import com.joakibj.tswrdb.rdb.export.RdbGenericDataExporter
import com.joakibj.tswrdb.rdb.RdbTypes
import com.joakibj.tswrdb.rdb.index.RdbIndexEntry

object StringCommands {
  val default = new StringExportCommand

  class StringExportCommand extends Command with ExitCommands {
    def execute(config: Config) {
      val stringRdbType = RdbTypes.strings
      println("Exporting RdbType: " + stringRdbType + " into exported/" + stringRdbType + " ...")

      val stringDataExporter = RdbStringDataExporter(new File(config.tswDirectory, "RDB"))

      config.language match {
        case StringLanguage.All =>
          println("Exporting ALL languages.")
          stringDataExporter.exportAll(stringRdbType)
        case _ => {
          println("Exporting language: " + config.language)
          val stringLanguageReader = new RdbStringLanguageIndexReader(new File(config.tswDirectory, "Data/text/" + config.language + ".tdbl"), config.language)
          val languageTable = stringLanguageReader.readEntries
          stringDataExporter.exportFiltered(stringRdbType, (ie: RdbIndexEntry) => languageTable.map(_.rdbId).contains(ie.id))
        }
      }
    }
  }

}

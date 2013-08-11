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
import com.joakibj.tswrdb.rdb.strings.{StringLanguage, RdbStringLanguageIndexReader, StringRdbDataXmlTransformer}
import com.joakibj.tswrdb.rdb.export.RdbExporter
import com.joakibj.tswrdb.rdb.RdbTypes

object StringCommands {
  val default = new StringExportCommand

  class StringExportCommand extends Command with ExitCommands {
    def execute(config: Config) {
      val stringRdbType = RdbTypes.strings

      println("TSW directory set to: " + config.tswDirectory.getCanonicalPath)
      println("Exporting RdbType: " + stringRdbType + " into exported/" + stringRdbType + " ...")

      config.language match {
        case StringLanguage.All =>
          println("Exporting ALL languages.")
          RdbExporter(new File(config.tswDirectory, "RDB"), new StringRdbDataXmlTransformer).exportAll(stringRdbType)
        case _ => {
          println("Exporting language: " + config.language)
          val stringLanguageReader = new RdbStringLanguageIndexReader(new File(config.tswDirectory, "Data/text/" + config.language + ".tdbl"), config.language)
          val languageTable = stringLanguageReader.readEntries
          RdbExporter(new File(config.tswDirectory, "RDB"), new StringRdbDataXmlTransformer).exportFiltered(stringRdbType, languageTable.map((category) => category.rdbId))
        }
      }
    }
  }

}

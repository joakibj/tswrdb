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
import com.joakibj.tswrdb.rdb.strings.{RdbStringDataExporter, RdbStringLanguageIndexReader}
import com.joakibj.tswrdb.rdb.RdbTypes
import com.joakibj.tswrdb.rdb.index.RdbIndexEntry

object StringCommands {
  val default = new StringExportCommand

  class StringExportCommand extends Command with ExitCommands {
    def execute(config: Config) {
      val stringRdbType = RdbTypes.Strings
      println("Exporting RdbType: " + stringRdbType + " into exported/" + stringRdbType + " ...")

      println("Exporting language: " + config.language)
      val languageTableFileName = "Data/text/" + config.language + ".tdbl"
      val stringLanguageReader = new RdbStringLanguageIndexReader(new File(config.tswDirectory, languageTableFileName), config.language)
      val languageTable = stringLanguageReader.readEntries()
      val stringDataExporter = RdbStringDataExporter(new File(config.tswDirectory, "RDB"), languageTable)
      stringDataExporter.exportFiltered(stringRdbType, (ie: RdbIndexEntry) => languageTable.map(_.rdbId).contains(ie.id))
    }
  }

}

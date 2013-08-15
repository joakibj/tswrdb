/*
 * tswrdb - a program and API to export the TSW resource database
 *
 * Copyright (C) 2013 Joakim Bjørnstad <joakibj@gmail.com>
 *
 * Licensed under the GNU General Public License version 2.
 * Please see the LICENSE file for the license text in verbatim.
 */

package com.joakibj.tswrdb.rdb.strings

import java.io.File
import com.joakibj.tswrdb.rdb.export.{DataFileWriter, RdbDataEntry, RdbDataExporter}
import com.joakibj.tswrdb.rdb.{RdbTypes, RdbIOException, RdbType}
import com.joakibj.tswrdb.rdb.index.RdbIndexEntry

object StringExportFormat extends Enumeration {
  val Xml = Value("xml")
  val Json = Value("json")
}

object RdbStringDataExporter {
  def apply(rdbFilename: String, language: StringLanguage.Value, exportFormat: StringExportFormat.Value) =
    new RdbStringDataExporter(new File(rdbFilename), language, exportFormat)

  def apply(rdbDataDirectory: File, language: StringLanguage.Value, exportFormat: StringExportFormat.Value) =
    new RdbStringDataExporter(rdbDataDirectory, language, exportFormat)
}

class RdbStringDataExporter(rdbDataDirectory: File, language: StringLanguage.Value, exportFormat: StringExportFormat.Value) extends RdbDataExporter(rdbDataDirectory) {
  val postDataTransformer = exportFormat match {
    case StringExportFormat.Xml => new RdbStringDataXmlTransformer
    case StringExportFormat.Json => new RdbStringDataJsonTransformer
  }
  val languageTable = getLanguageTable()

  def exportStrings() {
    exportFiltered(RdbTypes.Strings, (ie: RdbIndexEntry) => languageTable.map(_.rdbId).contains(ie.id))
  }

  protected def exportDataToFile(rdbType: RdbType, outputDirectory: File, dataEntry: RdbDataEntry, buf: Array[Byte]) {
    val filename = mapToCanonicalName(dataEntry.id) + "." + exportFormat
    val fileWriter = DataFileWriter(new File(outputDirectory, filename))
    fileWriter.writeData(buf)
  }

  private def mapToCanonicalName(dataEntryId: Int) = {
    languageTable.find((stringLangIndexEntry) => stringLangIndexEntry.rdbId == dataEntryId).getOrElse {
      throw new RdbIOException("String DataEntry did not have a matching Category")
    }.category.name
  }

  private def getLanguageTable() = {
    val languageTableFileName = "../Data/text/" + language + ".tdbl"
    val stringLanguageReader = new RdbStringLanguageIndexReader(new File(rdbDataDirectory, languageTableFileName), language)
    val languageTable = stringLanguageReader.readEntries()
    stringLanguageReader.close()

    languageTable
  }
}
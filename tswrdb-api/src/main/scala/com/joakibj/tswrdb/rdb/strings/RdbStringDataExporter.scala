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
import com.joakibj.tswrdb.rdb.{RdbIOException, RdbType}

object RdbStringDataExporter {
  def apply(rdbFilename: String, stringIndexTable: List[RdbStringLangIndexEntry]) =
    new RdbStringDataExporter(new File(rdbFilename), stringIndexTable)

  def apply(rdbDataDirectory: File, stringIndexTable: List[RdbStringLangIndexEntry]) =
    new RdbStringDataExporter(rdbDataDirectory, stringIndexTable)
}

class RdbStringDataExporter(rdbDataDirectory: File, stringIndexTable: List[RdbStringLangIndexEntry]) extends RdbDataExporter(rdbDataDirectory) {
  val postDataTransformer = new RdbStringDataXmlTransformer

  protected def exportDataToFile(rdbType: RdbType, outputDirectory: File, dataEntry: RdbDataEntry, buf: Array[Byte]) {
    val filename = mapToCanonicalName(dataEntry.id) + ".xml"
    val fileWriter = DataFileWriter(new File(outputDirectory, filename))
    fileWriter.writeData(buf)
  }

  private def mapToCanonicalName(dataEntryId: Int) = {
    stringIndexTable.find((stringLangIndexEntry) => stringLangIndexEntry.rdbId == dataEntryId).getOrElse {
      throw new RdbIOException("String DataEntry did not have a matching Category")
    }.category.name
  }
}
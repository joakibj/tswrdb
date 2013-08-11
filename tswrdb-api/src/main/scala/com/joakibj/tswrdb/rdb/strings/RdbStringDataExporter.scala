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
import com.joakibj.tswrdb.rdb.export.{RdbDataEntry, EncodedDataFileWriter, RdbDataExporter}
import com.joakibj.tswrdb.rdb.{RdbIOException, RdbType}

object RdbStringDataExporter {
  def apply(rdbFilename: String, languageTable: List[RdbStringCategory]) =
    new RdbStringDataExporter(new File(rdbFilename), languageTable)

  def apply(rdbDataDirectory: File, languageTable: List[RdbStringCategory]) =
    new RdbStringDataExporter(rdbDataDirectory, languageTable)
}

class RdbStringDataExporter(rdbDataDirectory: File, languageTable: List[RdbStringCategory]) extends RdbDataExporter(rdbDataDirectory) {
  val postDataTransformer = new StringRdbDataXmlTransformer

  protected def exportDataToFile(rdbType: RdbType, outputDirectory: File, dataEntry: RdbDataEntry, buf: Array[Byte]) {
    val filename = mapToCanonicalName(dataEntry.id) + "." + rdbType.fileType.extension
    val fileWriter = EncodedDataFileWriter(new File(outputDirectory, filename))
    fileWriter.writeData(buf)
  }

  private def mapToCanonicalName(dataEntryId: Int) = {
    val (catName1, catName2) = languageTable.find((stringCat) => stringCat.rdbId == dataEntryId).getOrElse {
      throw new RdbIOException("String DataEntry did not have a matching Category")
    }.categoryNamePair

    catName1 + "_" + catName2
  }
}
/*
 * tswrdb - a program and API to export the TSW resource database
 *
 * Copyright (C) 2013 Joakim Bjørnstad <joakibj@gmail.com>
 *
 * Licensed under the GNU General Public License version 2.
 * Please see the LICENSE file for the license text in verbatim.
 */

package com.joakibj.tswrdb.rdb.export

import java.io.File
import com.joakibj.tswrdb.rdb._

object RdbGenericDataExporter {
  def apply(rdbFilename: String) =
    new RdbGenericDataExporter(new File(rdbFilename))

  def apply(rdbDataDirectory: File) =
    new RdbGenericDataExporter(rdbDataDirectory)
}

class RdbGenericDataExporter(rdbDataDirectory: File) extends RdbDataExporter(rdbDataDirectory) {
  val postDataTransformer = new NoRdbDataTransformer

  protected def exportDataToFile(rdbType: RdbType, outputDirectory: File, dataEntry: RdbDataEntry, buf: Array[Byte]) {
    val filename = dataEntry.id + "." + rdbType.fileType.extension
    val fileWriter = DataFileWriter(new File(outputDirectory, filename))
    fileWriter.writeData(buf)
  }
}

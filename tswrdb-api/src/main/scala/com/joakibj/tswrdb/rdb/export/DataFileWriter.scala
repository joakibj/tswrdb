/*
 * tswrdb - a program and API to export the TSW resource database
 *
 * Copyright (C) 2013 Joakim Bjørnstad <joakibj@gmail.com>
 *
 * Licensed under the GNU General Public License version 2.
 * Please see the LICENSE file for the license text in verbatim.
 */

package com.joakibj.tswrdb.rdb.export

import java.io.{FileOutputStream, File}

object DataFileWriter {
  def apply(outputFile: File) = new DataFileWriter(outputFile)
}

class DataFileWriter(outputFile: File) {

  def writeData(buf: Array[Byte]) {
    val fos = new FileOutputStream(outputFile)
    fos.write(buf)
    fos.close()
    //println("Written: " + outputFile.getName)
  }
}

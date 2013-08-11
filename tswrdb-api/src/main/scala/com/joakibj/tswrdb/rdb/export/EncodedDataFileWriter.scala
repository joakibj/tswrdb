/*
 * tswrdb - a program and API to export the TSW resource database
 *
 * Copyright (C) 2013 Joakim Bjørnstad <joakibj@gmail.com>
 *
 * Licensed under the GNU General Public License version 2.
 * Please see the LICENSE file for the license text in verbatim.
 */

package com.joakibj.tswrdb.rdb.export

import com.joakibj.tswrdb.rdb.DataWriter
import java.io.{File, OutputStreamWriter, FileOutputStream}

object EncodedDataFileWriter {
  def apply(outputFile: File) = new EncodedDataFileWriter(outputFile)
}

class EncodedDataFileWriter(outputFile: File) extends DataWriter {
  def writeData(buf: Array[Byte]) {
    val ousw = new OutputStreamWriter(new FileOutputStream(outputFile), "UTF-8")
    ousw.write(buf.map(_.toChar), 0, buf.length)
    ousw.close()
  }
}

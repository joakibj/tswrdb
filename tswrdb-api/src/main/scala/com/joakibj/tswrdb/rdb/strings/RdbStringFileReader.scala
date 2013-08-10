/*
 * tswrdb - a program and API to export the TSW resource database
 *
 * Copyright (C) 2013 Joakim Bjørnstad <joakibj@gmail.com>
 *
 * Licensed under the GNU General Public License version 2.
 * Please see the LICENSE file for the license text in verbatim.
 */

package com.joakibj.tswrdb.rdb.strings

import com.joakibj.tswrdb.rdb.{Severity, RdbIOException, RdbFileReader}
import java.io.{File, FileOutputStream, ByteArrayInputStream, InputStream}
import com.joakibj.tswrdb.rdb.util.ByteUtils
import collection.mutable.ArrayBuffer

class RdbStringIndexTable(val header: RdbStringHeader,
                          val table: ArrayBuffer[RdbStringIndexEntry]) {

}

class RdbStringIndexEntry(val index: Int, val unknown: Int, val offset: Int, val length: Int)

class RdbStringHeader(val category: Int,
                      val flags: Int,
                      val stringDataLength: Int,
                      val numStrings: Int,
                      val hash: Array[Byte]) extends ByteUtils {
}

class RdbStringFileReader(buf: Array[Byte]) extends RdbFileReader {
  require(buf.length > 0, "Length must be greater than 0")

  val MagicNumber: String = "TDC2"
  val inputStream: InputStream = new ByteArrayInputStream(buf)

  require(hasMagicNumber(), "Buf does not have the required MagicNumber and is not a string file")

  val header = readHeader()

  def getStrings() = {
    val strings = ArrayBuffer[(Int, String)]()
    val stringbuf: Array[Byte] = new Array(header.stringDataLength)
    inputStream.read(stringbuf)
    val indexTable = readIndexTable()

    indexTable.table.foreach {
      (ie: RdbStringIndexEntry) => {
        val buf = stringbuf.slice(ie.offset, ie.offset + ie.length)
        strings += Tuple2(ie.index, new String(buf))
      }
    }

    strings
  }

  private def readIndexTable() = {
    val indexEntries = ArrayBuffer[RdbStringIndexEntry]()

    for (i <- 0 until header.numStrings) {
      indexEntries += readNextIndexEntry()
    }

    new RdbStringIndexTable(header, indexEntries)
  }

  private def readNextIndexEntry() = {
    val buf: Array[Byte] = new Array(16)

    if (inputStream.read(buf, 0, 16) != -1) {
      val index = littleEndianInt(buf.slice(0, 4))
      val unknown = littleEndianInt(buf.slice(4, 8))
      val offset = littleEndianInt(buf.slice(8, 12))
      val length = littleEndianInt(buf.slice(12, 16))

      new RdbStringIndexEntry(index, unknown, offset, length)
    } else {
      throw new RdbIOException("Prematurely got to end of file", Severity.Mayan)
    }
  }

  private def readHeader() = {
    val buf: Array[Byte] = new Array(32)
    inputStream.read(buf, 0, 32)
    val category = littleEndianInt(buf.slice(0, 4))
    val flags = littleEndianInt(buf.slice(4, 8))
    val stringLength = littleEndianInt(buf.slice(8, 12))
    val numStrings = littleEndianInt(buf.slice(12, 16))
    val hash = littleEndianArray(buf.slice(16, 32))

    new RdbStringHeader(category, flags, stringLength, numStrings, hash)
  }


}

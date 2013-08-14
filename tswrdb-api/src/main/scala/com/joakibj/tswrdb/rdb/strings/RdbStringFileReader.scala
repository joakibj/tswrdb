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
import java.io.{ByteArrayInputStream, InputStream}
import com.joakibj.tswrdb.rdb.util.ByteUtils
import collection.mutable.ArrayBuffer

class RdbStringIndexTable(val header: RdbStringHeader,
                          val table: ArrayBuffer[RdbStringIndexEntry])

case class RdbStringIndexEntry(index: Int,
                               unknown: Int,
                               offset: Int,
                               length: Int)

case class RdbStringHeader(category: Int,
                           flags: Int,
                           stringDataLength: Int,
                           numStrings: Int,
                           hash: Array[Byte]) extends ByteUtils

case class RdbString(stringId: Int,
                     content: String) {
  def toXml = {
    <rdbString>
      <stringId>{stringId}
      </stringId>
      <content>{content}</content>
    </rdbString>
  }
}

class RdbStringFileReader(buf: Array[Byte]) extends RdbFileReader {
  require(buf.length > 0, "Length must be greater than 0")

  val MagicNumber: String = "TDC2"
  val inputStream: InputStream = new ByteArrayInputStream(buf)

  require(hasMagicNumber(), "Buf does not have the required MagicNumber and is not a string file")

  val header = readHeader()

  def getStrings() = {
    val stringBuf: Array[Byte] = readLen(header.stringDataLength)
    val indexTable = readIndexTable()

    val strings = for {
      ie <- indexTable.table
      buf = stringBuf.slice(ie.offset, ie.offset + ie.length)
      if(buf.size > 0)
    } yield RdbString(ie.index, new String(buf, "UTF-8"))

    (header, strings.toVector)
  }

  private def readIndexTable() = {
    val indexEntries = ArrayBuffer[RdbStringIndexEntry]()

    for (i <- 0 until header.numStrings) {
      indexEntries += readNextIndexEntry()
    }

    new RdbStringIndexTable(header, indexEntries)
  }

  private def readNextIndexEntry() = {
    val index = readInt()
    val unknown = readInt()
    val offset = readInt()
    val length = readInt()

    new RdbStringIndexEntry(index, unknown, offset, length)
  }

  private def readHeader() = {
    val category = readInt()
    val flags = readInt()
    val stringLength = readInt()
    val numStrings = readInt()
    val hash = readLen(16)

    new RdbStringHeader(category, flags, stringLength, numStrings, hash)
  }

}

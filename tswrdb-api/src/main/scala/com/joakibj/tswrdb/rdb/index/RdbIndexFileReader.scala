/*
 * tswrdb - a program and API to export the TSW resource database
 *
 * Copyright (C) 2013 Joakim Bjørnstad <joakibj@gmail.com>
 *
 * Licensed under the GNU General Public License version 2.
 * Please see the LICENSE file for the license text in verbatim.
 */

package com.joakibj.tswrdb.rdb.index

import java.io.{FileInputStream, File}
import scala.collection.mutable.ArrayBuffer
import com.joakibj.tswrdb.rdb._
import com.joakibj.tswrdb.rdb.RdbIOException

object RdbIndexFileReader {
  def apply(file: File) = new RdbIndexFileReader(file)
}

class RdbIndexFileReader(file: File) extends RdbFileReader {
  require(file.isFile, "Must be a file")

  val MagicNumber: String = "IBDR"
  val fileInputStream = new FileInputStream(file)

  require(hasMagicNumber(), "File does not have the required MagicNumber and is not the index file")

  val indexHeader = readIndexHeader()

  def getIndexTable: RdbDataIndexTable = new RdbDataIndexTable(indexHeader, readIndexEntries())

  def closeFile() { fileInputStream.close() }
  
  private def readIndexEntries(): ArrayBuffer[RdbIndexEntry] = {

    val numEntries = indexHeader.numEntries
    val indexTable = ArrayBuffer[(Int, Int)]()
    for (i <- 0 until numEntries) {
      val indexEntry = readNextIndexEntry()
      indexTable += indexEntry
    }

    val indexEntries = ArrayBuffer[RdbIndexEntry]()
    for (i <- 0 until numEntries) {
      val indexEntryDetails = readNextIndexEntryDetail()
      val indexEntry = indexTable(i)
      indexEntries += RdbIndexEntry(indexEntry, indexEntryDetails)
    }
    closeFile()

    indexEntries
  }

  private def readNextIndexEntry(): (Int, Int) = {
    val buf: Array[Byte] = new Array(8)
    if (fileInputStream.read(buf, 0, 8) != -1) {
      val splitBuf = buf.splitAt(4)
      val rdbType = littleEndianInt(splitBuf._1)
      val rdbId = littleEndianInt(splitBuf._2)

      (rdbType, rdbId)
    } else {
      throw new RdbIOException("Prematurely got to end of file", Severity.Mayan)
    }
  }

  private def readNextIndexEntryDetail(): (Byte, Int, Int, Array[Byte]) = {
    val buf: Array[Byte] = new Array(28)
    if (fileInputStream.read(buf, 0, 28) != -1) {
      val fileNum = littleEndianByte(buf.slice(0, 1))
      val offset = littleEndianInt(buf.slice(4, 8))
      val length = littleEndianInt(buf.slice(8, 12))
      val hash = buf.slice(12, 28)

      (fileNum, offset, length, hash)
    } else {
      throw new RdbIOException("Prematurely got to end of file", Severity.Mayan)
    }
  }

  private def readIndexHeader(): RdbIndexHeader = {
    var buf: Array[Byte] = new Array(24)
    fileInputStream.read(buf, 0, 24)
    val version = littleEndianInt(buf.slice(0, 4))
    val hash = littleEndianArray(buf.slice(4, 20))
    val numEntries = littleEndianInt(buf.slice(20, 24))

    RdbIndexHeader(version, hash, numEntries)
  }
}

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
  val inputStream = new FileInputStream(file)

  require(hasMagicNumber(), "File does not have the required MagicNumber and is not the index file")

  val indexHeader = readIndexHeader()

  def getIndexTable: RdbDataIndexTable = new RdbDataIndexTable(indexHeader, readIndexEntries())

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

    indexEntries
  }

  private def readNextIndexEntry(): (Int, Int) = {
    val rdbType = readInt()
    val rdbId = readInt()

    (rdbType, rdbId)
  }

  private def readNextIndexEntryDetail(): (Byte, Int, Int, Array[Byte]) = {
    val fileNum = readByte()
    inputStream.skip(3) // unknown 3 bytes
    val offset = readInt()
    val length = readInt()
    val hash = readLen(16)

    (fileNum, offset, length, hash)
  }

  private def readIndexHeader(): RdbIndexHeader = {
    val version = readInt()
    val hash = readLen(16)
    val numEntries = readInt()

    RdbIndexHeader(version, hash, numEntries)
  }
}

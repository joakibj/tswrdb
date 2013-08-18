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
import com.joakibj.tswrdb.rdb._

object RdbIndexFileReader {
  def apply(file: File) = new RdbIndexFileReader(file)
}

class RdbIndexFileReader(file: File) extends RdbFileReader {
  require(file.isFile, "Must be a file")

  val MagicNumber: String = "IBDR"
  val inputStream = new FileInputStream(file)

  require(hasMagicNumber(), "File does not have the required MagicNumber and is not the index file")

  val indexHeader = readIndexHeader()

  def getIndexTable: RdbDataIndexTable =
    new RdbDataIndexTable(indexHeader, readIndexEntries())

  private def readIndexEntries() = {
    val numEntries = indexHeader.numEntries
    val indexTable =
      for {
        i <- 0 until numEntries
      } yield readNextIndexEntry()

    val indexEntries =
      for {
        i <- 0 until numEntries
      } yield RdbIndexEntry(indexTable(i), readNextIndexEntryDetail())

    indexEntries.toVector
  }

  private def readNextIndexEntry() = {
    val rdbType = readInt()
    val rdbId = readInt()

    (rdbType, rdbId)
  }

  private def readNextIndexEntryDetail() = {
    val fileNum = readByte()
    inputStream.skip(3) // unknown 3 bytes
    val offset = readInt()
    val length = readInt()
    val hash = readHash()

    (fileNum, offset, length, hash)
  }

  private def readIndexHeader() = {
    val version = readInt()
    val hash = readHash()
    val numEntries = readInt()

    RdbIndexHeader(version, hash, numEntries)
  }
}

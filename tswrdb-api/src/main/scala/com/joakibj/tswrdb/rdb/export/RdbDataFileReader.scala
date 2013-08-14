/*
 * tswrdb - a program and API to export the TSW resource database
 *
 * Copyright (C) 2013 Joakim Bjørnstad <joakibj@gmail.com>
 *
 * Licensed under the GNU General Public License version 2.
 * Please see the LICENSE file for the license text in verbatim.
 */

package com.joakibj.tswrdb.rdb.export

import java.io.{FileOutputStream, FileInputStream, File}
import com.joakibj.tswrdb.rdb.index.RdbIndexEntry
import com.joakibj.tswrdb.rdb.{RdbTypeNotFoundException, RdbIOException, RdbTypes, RdbFileReader}
import com.joakibj.tswrdb.rdb.util.ByteUtils

object RdbDataEntry {
  val HeaderSize = 16

  def apply(rdbType: Int, id: Int, length: Int) =
    new RdbDataEntry(rdbType, id, length)

  def apply(rdbType: Int, id: Int, length: Int, buf: Array[Byte]) =
    new RdbDataEntry(rdbType, id, length, buf)
}

class RdbDataEntry(val rdbType: Int,
                   val id: Int,
                   val length: Int,
                   val buf: Array[Byte]) extends ByteUtils {
  def this(rdbType: Int,
           id: Int,
           length: Int) = this(rdbType, id, length, new Array[Byte](0))

  def toArray: Array[Byte] =
    intToBytes(rdbType) ++ intToBytes(id) ++ intToBytes(length) ++ padding(4) ++ buf

  override def equals(other: Any) = other match {
    case that: RdbDataEntry => {
      this.rdbType == that.rdbType &&
        this.id == that.id &&
        this.length == that.length
    }
    case that: RdbIndexEntry => {
      this.rdbType == that.rdbType &&
        this.id == that.id &&
        this.length == that.length
    }
    case _ => false
  }

  override def toString = {
    "(type: " + rdbType +
      ", id: " + id +
      ", length: " + length + ")"
  }
}

object RdbDataFileReader {
  def apply(rdbDataFile: File, indexEntries: Array[RdbIndexEntry]) =
    new RdbDataFileReader(rdbDataFile, indexEntries)
}

class RdbDataFileReader(rdbDataFile: File,
                        ie: Array[RdbIndexEntry]) extends RdbFileReader {
  require(rdbDataFile.isFile, "Datafile does not exist")

  val MagicNumber: String = "RDB0"
  val inputStream = new FileInputStream(rdbDataFile)

  require(hasMagicNumber(), "Datafile does not have the magic number")
  require(validIndexEntries(ie), "All index entries must be in file: " + rdbDataFile.getName)

  private val indexEntries = sortedEntries(ie)

  private def sortedEntries(indexEntries: Array[RdbIndexEntry]): Array[RdbIndexEntry] =
    indexEntries.sortBy(_.dataOffset)

  private def validIndexEntries(indexEntries: Array[RdbIndexEntry]): Boolean =
    indexEntries.count((indexEntry: RdbIndexEntry) => indexEntry.fileName == rdbDataFile.getName) == indexEntries.size

  def readDataEntries() = {
    if (indexEntries.length == 0) Vector()

    val firstIndexEntry = indexEntries(0)

    val (dataEntry1, buf1) = readDataEntry(firstIndexEntry, 4)
    val rdbType1 = findRdbType(firstIndexEntry.rdbType)

    val entries = for {
      ie <- indexEntries.sliding(2)
      indexEntry1 = ie.head
      indexEntry2 = ie.last
      (dataEntry2, buf2) = readDataEntry(indexEntry2, indexEntry1.dataOffset + indexEntry1.length)
      rdbType = findRdbType(indexEntry2.rdbType)
    } yield (dataEntry2, buf2.drop(rdbType.skipBytes))

    val allEntries = Vector((dataEntry1, buf1.drop(rdbType1.skipBytes))) ++ entries.toVector

    allEntries
  }

  def readDataEntry(indexEntry: RdbIndexEntry, skipBytes: Int): (RdbDataEntry, Array[Byte]) = {
    val dataEntry = readNextDataEntryHeader(indexEntry.dataOffset - skipBytes)
    if (isCorrectDataEntry(indexEntry, dataEntry)) {
      val buf = readData(indexEntry.length)

      (dataEntry, buf)
    } else {
      throw new RdbIOException("A mismatching data entry was read: " + indexEntry + " vs " + dataEntry)
    }
  }

  private def readNextDataEntryHeader(skipToNextOffset: Int): RdbDataEntry = {
    inputStream.skip(skipToNextOffset - RdbDataEntry.HeaderSize)

    val buf: Array[Byte] = new Array(RdbDataEntry.HeaderSize)
    inputStream.read(buf)

    val dataType = littleEndianInt(buf.slice(0, 4))
    val dataId = littleEndianInt(buf.slice(4, 8))
    val dataLength = littleEndianInt(buf.slice(8, 12))

    RdbDataEntry(dataType, dataId, dataLength)
  }

  private def readData(len: Int): Array[Byte] = {
    val buf: Array[Byte] = new Array(len)
    inputStream.read(buf)
    buf
  }

  private def isCorrectDataEntry(indexEntry: RdbIndexEntry, dataEntry: RdbDataEntry): Boolean = {
    dataEntry == indexEntry
  }

  private def findRdbType(rdbType: Int) = {
    RdbTypes.find(rdbType).getOrElse {
      throw new RdbTypeNotFoundException("RdbType: " + rdbType + " was not found")
    }
  }
}

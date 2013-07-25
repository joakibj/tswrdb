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

object RdbDataFileExporter {
  def apply(outputDirectory: File, rdbDataFile: File, indexEntries: Array[RdbIndexEntry]) =
    new RdbDataFileExporter(outputDirectory, rdbDataFile, indexEntries)
}

class RdbDataFileExporter(outputDirectory: File,
                          rdbDataFile: File,
                          ie: Array[RdbIndexEntry]) extends RdbFileReader {
  require(outputDirectory.isDirectory, "Output directory does not exist")
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

  def exportDataEntries() {

    val firstIndexEntry = indexEntries(0)
    readDataEntry(firstIndexEntry, 4)

    if (indexEntries.size == 1) return

    indexEntries.sliding(2).foreach {
      it =>
        val indexEntry1 = it.head
        val indexEntry2 = it.last
        val (_, buf) = readDataEntry(indexEntry2, indexEntry1.dataOffset + indexEntry1.length)

        val rdbType = RdbTypes.find(indexEntry2.rdbType).getOrElse {
          throw new RdbTypeNotFoundException("RdbType: " + indexEntry2.rdbType + " was not found")
        }
        val filename = indexEntry2.id + "." + rdbType.fileType.extension
        writeData(new File(outputDirectory, filename), buf.drop(rdbType.skipBytes))
    }
    inputStream.close()
  }

  def readDataEntry(indexEntry: RdbIndexEntry, skipBytes: Int): (RdbDataEntry, Array[Byte]) = {
    val dataEntry = readNextDataEntryHeader((indexEntry.dataOffset - 16) - skipBytes)
    if (isCorrectDataEntry(indexEntry, dataEntry)) {
      val buf = readData(indexEntry.length)

      (dataEntry, buf)
    } else {
      throw new RdbIOException("A mismatching data entry was read: " + indexEntry + " vs " + dataEntry)
    }
  }

  private def readNextDataEntryHeader(skipBytes: Int): RdbDataEntry = {
    inputStream.skip(skipBytes)

    val buf: Array[Byte] = new Array(16)
    inputStream.read(buf, 0, 16)

    val dataType = littleEndianInt(buf.slice(0, 4))
    val dataId = littleEndianInt(buf.slice(4, 8))
    val dataLength = littleEndianInt(buf.slice(8, 12))

    RdbDataEntry(dataType, dataId, dataLength)
  }

  private def readData(len: Int): Array[Byte] = {
    val buf: Array[Byte] = new Array(len)
    inputStream.read(buf, 0, len)
    buf
  }

  private def writeData(file: File, buf: Array[Byte]) {
    val fos = new FileOutputStream(file)
    fos.write(buf)
    fos.close()
    println("Written: " + file.getName)
  }

  private def isCorrectDataEntry(indexEntry: RdbIndexEntry, dataEntry: RdbDataEntry): Boolean = {
    dataEntry == indexEntry
  }
}

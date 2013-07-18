package com.joakibj.tswrdb.rdb

import java.io.{FileOutputStream, BufferedInputStream, FileInputStream, File}
import collection.mutable.ArrayBuffer
import java.nio.{ByteOrder, ByteBuffer}

object RdbDataEntry {
  def apply(rdbType: Int, id: Int, length: Int) = new RdbDataEntry(rdbType, id, length)
}

class RdbDataEntry(val rdbType: Int, val id: Int, val length: Int) {
  val buf: Array[Byte] = Array()

  override def toString = {
    "type: " + rdbType +
      ", id: " + id +
      ", length: " + length
  }
}

object RdbDataFileReader {
  def apply(file: File, indexEntries: Array[RdbIndexEntry]) = new RdbDataFileReader(file, indexEntries)
}

class RdbDataFileReader(file: File, ie: Array[RdbIndexEntry]) extends RdbFileReader {
  require(file.isFile, "Datafile does not exist")

  val MagicNumber: String = "RDB0"
  val fileInputStream = new FileInputStream(file)
  val dir = "D:\\Joakim\\dev\\tswrdb\\output\\"

  require(hasMagicNumber(), "Datafile does not have the magic number")
  require(validIndexEntries(ie), "All index entries must be in file: " + file.getName)

  private val indexEntries = sortedEntries(ie)

  private def sortedEntries(indexEntries: Array[RdbIndexEntry]): Array[RdbIndexEntry] = indexEntries.sortBy(_.dataOffset)

  private def validIndexEntries(indexEntries: Array[RdbIndexEntry]): Boolean =
    indexEntries.count((indexEntry: RdbIndexEntry) => indexEntry.fileName == file.getName) == indexEntries.size

  def readDataEntries() {

    val firstIndexEntry = indexEntries(0)
    processEntry(firstIndexEntry, 4)
    indexEntries.sliding(2).foreach {
      it =>
        val indexEntry1 = it.head
        val indexEntry2 = it.last
        val rdbType = RdbTypes.find(indexEntry1.rdbType).get
        processEntry(indexEntry2, indexEntry1.dataOffset + indexEntry1.length + rdbType.skipBytes)
    }
    fileInputStream.close()
  }

  private def processEntry(indexEntry: RdbIndexEntry, skipBytes: Int) {
    val dataEntry = readNextDataEntryHeader((indexEntry.dataOffset - 16) - skipBytes)
    if (isCorrectDataEntry(indexEntry, dataEntry)) {
      val rdbType = RdbTypes.find(indexEntry.rdbType).get
      val buf = readData(rdbType.skipBytes, dataEntry.length)
      val fileName = dir + indexEntry.id + "." + rdbType.fileType.extension
      writeData(new File(fileName), buf)
    } else {
      throw new RuntimeException("A mismatching data entry was read")
    }
  }

  private def readNextDataEntryHeader(skipBytes: Int): RdbDataEntry = {
    val skipped = fileInputStream.skip(skipBytes)

    val buf: Array[Byte] = new Array(16)
    fileInputStream.read(buf, 0, 16)

    val dataType = littleEndianInt(buf.slice(0, 4))
    val dataId = littleEndianInt(buf.slice(4, 8))
    val dataLength = littleEndianInt(buf.slice(8, 12))

    RdbDataEntry(dataType, dataId, dataLength)
  }

  private def readData(skipBytes: Int, len: Int): Array[Byte] = {
    fileInputStream.skip(skipBytes)
    val buf: Array[Byte] = new Array(len)
    fileInputStream.read(buf, 0, len)
    buf
  }

  private def writeData(file: File, buf: Array[Byte]) {
    val fos = new FileOutputStream(file)
    fos.write(buf)
    fos.close()
    println("Written: " + file.getName)
  }

  private def isCorrectDataEntry(indexEntry: RdbIndexEntry, dataEntry: RdbDataEntry): Boolean = {
    indexEntry.rdbType == dataEntry.rdbType &&
      indexEntry.id == dataEntry.id &&
      indexEntry.length == dataEntry.length
  }

  def toHex(buffer: Array[Byte]): String = buffer.map("%02x" format _).mkString
}

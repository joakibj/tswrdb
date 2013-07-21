package com.joakibj.tswrdb.rdb.export

import java.io.{FileOutputStream, FileInputStream, File}
import com.joakibj.tswrdb.rdb.index.RdbIndexEntry
import com.joakibj.tswrdb.rdb.{RdbIOException, RdbTypes, RdbFileReader}

object RdbDataEntry {
  def apply(rdbType: Int, id: Int, length: Int) = new RdbDataEntry(rdbType, id, length)
}

class RdbDataEntry(val rdbType: Int,
                   val id: Int,
                   val length: Int) {
  val buf: Array[Byte] = Array()

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
  val fileInputStream = new FileInputStream(rdbDataFile)

  require(hasMagicNumber(), "Datafile does not have the magic number")
  require(validIndexEntries(ie), "All index entries must be in file: " + rdbDataFile.getName)

  private val indexEntries = sortedEntries(ie)

  private def sortedEntries(indexEntries: Array[RdbIndexEntry]): Array[RdbIndexEntry] = indexEntries.sortBy(_.dataOffset)

  private def validIndexEntries(indexEntries: Array[RdbIndexEntry]): Boolean =
    indexEntries.count((indexEntry: RdbIndexEntry) => indexEntry.fileName == rdbDataFile.getName) == indexEntries.size

  def exportDataEntries() {

    val firstIndexEntry = indexEntries(0)
    processEntry(firstIndexEntry, 4)

    if (indexEntries.size == 1) return

    indexEntries.sliding(2).foreach {
      it =>
        val indexEntry1 = it.head
        val indexEntry2 = it.last
        processEntry(indexEntry2, indexEntry1.dataOffset + indexEntry1.length)
    }
    fileInputStream.close()
  }

  private def processEntry(indexEntry: RdbIndexEntry, skipBytes: Int) {
    val dataEntry = readNextDataEntryHeader((indexEntry.dataOffset - 16) - skipBytes)
    if (isCorrectDataEntry(indexEntry, dataEntry)) {
      val rdbType = RdbTypes.find(indexEntry.rdbType).get
      val buf = readData(indexEntry.length)
      val filename = indexEntry.id + "." + rdbType.fileType.extension
      writeData(new File(outputDirectory, filename), buf.drop(rdbType.skipBytes))
    } else {
      throw new RdbIOException("A mismatching data entry was read")
    }
  }

  private def readNextDataEntryHeader(skipBytes: Int): RdbDataEntry = {
    fileInputStream.skip(skipBytes)

    val buf: Array[Byte] = new Array(16)
    fileInputStream.read(buf, 0, 16)

    val dataType = littleEndianInt(buf.slice(0, 4))
    val dataId = littleEndianInt(buf.slice(4, 8))
    val dataLength = littleEndianInt(buf.slice(8, 12))

    RdbDataEntry(dataType, dataId, dataLength)
  }

  private def readData(len: Int): Array[Byte] = {
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
}

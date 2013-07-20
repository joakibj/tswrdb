package com.joakibj.tswrdb.rdb

import java.io.{FileInputStream, File}
import scala.collection.mutable.ArrayBuffer

object RdbIndexFileReader {
  def apply(file: File) = new RdbIndexFileReader(file)
}

class RdbIndexFileReader(file: File) extends RdbFileReader {
  require(file.isFile, "Must be a file")

  val MagicNumber: String = "IBDR"
  val fileInputStream = new FileInputStream(file)

  require(hasMagicNumber(), "File does not have the required MagicNumber and is not the index file")

  val numEntries = readNumIndexEntries

  def getIndexTable: RdbDataIndexTable = new RdbDataIndexTable(readIndexEntries())
  
  def readIndexEntries(): ArrayBuffer[RdbIndexEntry] = {
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
    fileInputStream.close()

    indexEntries
  }

  private def readNextIndexEntry(): (Int, Int) = {
    var buf: Array[Byte] = new Array(8)
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
    var buf: Array[Byte] = new Array(28)
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

  def toHex(buffer: Array[Byte]): String = buffer.map("%02X" format _).mkString

  private def readNumIndexEntries: Int = {
    var buf: Array[Byte] = new Array(4)
    fileInputStream.skip(20)
    fileInputStream.read(buf, 0, 4)
    littleEndianInt(buf)
  }
}

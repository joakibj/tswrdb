package com.joakibj.tswrdb.rdb

import java.io.{BufferedInputStream, FileInputStream, File}
import java.nio.{ByteOrder, ByteBuffer}
import scala.collection.mutable.ArrayBuffer

object RdbIndexEntry {
  def apply(tup2: Tuple2[Int, Int], tup4: Tuple4[Byte, Int, Int, Array[Byte]]) =
    new RdbIndexEntry(tup2._1, tup2._2, tup4._1, tup4._2, tup4._3, tup4._4)
}

class RdbIndexEntry(rdbType: Int, id: Int, fileNum: Byte, dataOffset: Int, length: Int, hash: Array[Byte]) {
  override def toString = {
    "type: " + rdbType +
      ", id: " + id +
      ", fileNum: " + fileNum +
      ", dataOffset: " + dataOffset +
      ", length: " + length
  }
}

object RdbIndexFileReader {
  def apply(file: File) = new RdbIndexFileReader(file)
}

class RdbIndexFileReader(file: File) extends RdbFileReader {
  require(file.isFile, "Must be a file")

  val MagicNumber: String = "IBDR"
  private val fileInputStream = new FileInputStream(file)
  private val bufferedInputStream = new BufferedInputStream(fileInputStream)
  bufferedInputStream.mark(8)
  private val littleEndianInt = ByteBuffer.wrap(_: Array[Byte]).order(ByteOrder.LITTLE_ENDIAN).getInt
  private val littleEndianByte = ByteBuffer.wrap(_: Array[Byte]).order(ByteOrder.LITTLE_ENDIAN).get
  private val littleEndianArray = ByteBuffer.wrap(_: Array[Byte]).order(ByteOrder.LITTLE_ENDIAN).array

  require(isIndexFile, "Must be an Index file")

  val numEntries = readNumIndexEntries

  def readIndexEntries: ArrayBuffer[RdbIndexEntry] = {
    bufferedInputStream.reset()
    bufferedInputStream.skip(28)
    val indexTable = ArrayBuffer[(Int, Int)]()
    println("Found " + numEntries + " index entries.")
    for (i <- 0 until numEntries) {
      val indexEntry = readIndexEntry(i)
      indexTable += indexEntry
    }

    val indexEntries = ArrayBuffer[RdbIndexEntry]()
    for (i <- 0 until numEntries) {
      val indexEntryDetails = readIndexEntryDetails(i)
      val indexEntry = indexTable(i)
      indexEntries += RdbIndexEntry(indexEntry, indexEntryDetails)
    }

    indexEntries
  }

  private def readIndexEntry(indexNum: Int): (Int, Int) = {
    var buf: Array[Byte] = new Array(8)
    if (bufferedInputStream.read(buf, 0, 8) != -1) {
      val splitBuf = buf.splitAt(4)
      val rdbType = littleEndianInt(splitBuf._1)
      val rdbIndex = littleEndianInt(splitBuf._2)

      (rdbType, rdbIndex)
    } else {
      throw new RuntimeException("Prematurely got to end of file")
    }
  }

  private def readIndexEntryDetails(indexNum: Int) = {
    var buf2: Array[Byte] = new Array(28)
    if (bufferedInputStream.read(buf2, 0, 28) != -1) {
      val fileNum = littleEndianByte(buf2.slice(0, 1))
      val offset = littleEndianInt(buf2.slice(4, 8))
      val length = littleEndianInt(buf2.slice(8, 12))
      val hash = buf2.slice(12, 28)

      (fileNum, offset, length, hash)
    } else {
      throw new RuntimeException("Prematurely got to end of file")
    }
  }

  def toHex(buffer: Array[Byte]): String = buffer.map("%02X" format _).mkString

  private def readNumIndexEntries: Int = {
    var buf: Array[Byte] = new Array(4)
    bufferedInputStream.skip(20)
    bufferedInputStream.read(buf, 0, 4)
    littleEndianInt(buf)
  }

  private def isIndexFile: Boolean = {
    var buf: Array[Byte] = new Array(4)
    bufferedInputStream.read(buf, 0, 4)
    if (new String(buf) == MagicNumber) true else false
  }
}

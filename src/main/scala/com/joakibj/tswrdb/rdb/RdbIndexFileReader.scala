package com.joakibj.tswrdb.rdb

import java.io.{BufferedInputStream, FileInputStream, File}
import java.nio.{ByteOrder, ByteBuffer}
import scala.collection.mutable.ArrayBuffer

object RdbIndexEntry {
  def apply(tup2: Tuple2[Int, Int], tup4: Tuple4[Byte, Int, Int, Array[Byte]]) =
    new RdbIndexEntry(tup2._1, tup2._2, tup4._1, tup4._2, tup4._3, tup4._4)
}

class RdbIndexEntry(val rdbType: Int,
                    val id: Int,
                    val fileNum: Byte,
                    val dataOffset: Int,
                    val length: Int,
                    val hash: Array[Byte]) {
  val fileName = "%02d.rdbdata" format fileNum
  override def toString = {
    "type: " + rdbType +
      ", id: " + id +
      ", fileNum: " + fileNum +
      ", fileName: " + fileName +
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
  val fileInputStream = new FileInputStream(file)
  val bufferedInputStream = new BufferedInputStream(fileInputStream)

  require(hasMagicNumber(), "File does not have the required MagicNumber and is not the index file")

  val numEntries = readNumIndexEntries

  def readIndexEntries: ArrayBuffer[RdbIndexEntry] = {
    bufferedInputStream.skip(28)
    val indexTable = ArrayBuffer[(Int, Int)]()
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
    bufferedInputStream.close()

    indexEntries
  }

  private def readIndexEntry(indexNum: Int): (Int, Int) = {
    var buf: Array[Byte] = new Array(8)
    if (bufferedInputStream.read(buf, 0, 8) != -1) {
      val splitBuf = buf.splitAt(4)
      val rdbType = littleEndianInt(splitBuf._1)
      val rdbId = littleEndianInt(splitBuf._2)

      (rdbId, rdbType)
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
    bufferedInputStream.skip(24)
    bufferedInputStream.read(buf, 0, 4)
    littleEndianInt(buf)
  }
}

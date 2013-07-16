package com.joakibj.tswrdb.rdb

import java.io.{BufferedInputStream, FileInputStream, File}
import io.Source
import java.nio.{ByteOrder, ByteBuffer}
import collection.mutable.ListBuffer

class RdbIndexEntry(rdbType: Int, id: Int, fileNum: Byte = 0, dataOffset: Int = 0, length: Int = 0, hash: Array[Byte] = Array()) {
  override def toString = "type: " + rdbType + ", id: " + id
}

object RdbIndexFileReader {
  def apply(file: File) = new RdbIndexFileReader(file)
}

class RdbIndexFileReader(file: File) extends RdbFileReader {
  require(file.isFile, "Must be a file")

  val MagicNumber: String = "IBDR"
  private val fileInputStream = new FileInputStream(file)
  private val bufferedInputStream = new BufferedInputStream(fileInputStream)
  private val littleEndianInt = ByteBuffer.wrap(_: Array[Byte]).order(ByteOrder.LITTLE_ENDIAN).getInt

  require(isIndexFile, "Must be an Index file")

  def readIndexEntries: ListBuffer[RdbIndexEntry] = {
    val listOfIndexEntries = ListBuffer[RdbIndexEntry]()
    val numEntries = readNumIndexEntries
    println("Found " + numEntries + " index entries.")
    for(i <- 0 until numEntries) {
      val indexEntry = readIndexEntry
      listOfIndexEntries += indexEntry
    }
    for (i <- 0 until numEntries) {
      val
    }
    println(listOfIndexEntries.size)

    listOfIndexEntries
  }

  private def readIndexEntry: RdbIndexEntry = {
    var buf: Array[Byte] = new Array(8)
    if(bufferedInputStream.read(buf, 0, 8) != -1) {
      val splitBuf = buf.splitAt(4)
      val rdbType = littleEndianInt(splitBuf._1)
      val rdbIndex = littleEndianInt(splitBuf._2)
      new RdbIndexEntry(rdbType, rdbIndex)
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
    if(new String(buf) == MagicNumber) true else false
  }
}

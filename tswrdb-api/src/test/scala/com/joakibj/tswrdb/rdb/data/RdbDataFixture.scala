package com.joakibj.tswrdb.rdb.data

import java.nio.{ByteOrder, ByteBuffer}
import collection.mutable.ArrayBuffer

object RdbDataFixture {
  val DummyHash = Array.fill(16)(0.toByte)

  def intToBytes(i: Int): Array[Byte] = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(i).array
  def byteToBytes(b: Byte): Array[Byte] = Array(b)
  def padding(num: Int): Array[Byte] = Array.fill(num)(0.toByte)
  def toHex(buffer: Array[Byte]): String = buffer.map("%02X" format _).mkString

  def generateTestData: Array[Byte] = {
    val MagicNumber: Array[Byte] = "IBDR" map(_.toByte) toArray
    val header: Array[Byte] =  MagicNumber ++
      intToBytes(7) ++
      padding(16) ++
      intToBytes(10)

    header ++
      generateIndexTable(10) ++
      generateIndexEntries(10)
  }

  def generateIndexTable(num: Int): Array[Byte] = {
    val table: ArrayBuffer[Byte] = ArrayBuffer()

    (1 to num).foreach {
      (i) =>
        var entry = Array[Byte]()
        if(i <= 5)
          entry = intToBytes(100000) ++
            intToBytes(i)
        else
          entry = intToBytes(100001) ++
            intToBytes(i)
        table ++= entry
    }

    table.toArray
  }

  def generateIndexEntries(num: Int): Array[Byte] = {
    val entries: ArrayBuffer[Byte] = ArrayBuffer()

    (0 until num).foreach {
      (i) =>
        var entry = Array[Byte]()
        if(i < 5)
          entry = byteToBytes(1) ++
            padding(3) ++
            intToBytes(i * 15) ++
            intToBytes(15) ++
            DummyHash
        else
          entry = byteToBytes(2) ++
            padding(3) ++
            intToBytes((5 * 15) +  (i - 5) * 10) ++
            intToBytes(10) ++
            DummyHash
        entries ++= entry
    }

    entries.toArray
  }
}

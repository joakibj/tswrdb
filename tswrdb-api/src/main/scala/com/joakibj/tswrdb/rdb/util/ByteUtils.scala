package com.joakibj.tswrdb.rdb.util

import java.nio.{ByteOrder, ByteBuffer}

trait ByteUtils {
  def intToBytes(i: Int): Array[Byte] = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(i).array
  def byteToBytes(b: Byte): Array[Byte] = Array(b)
  def padding(num: Int): Array[Byte] = Array.fill(num)(0.toByte)
  def toHex(buffer: Array[Byte]): String = buffer.map("%02X" format _).mkString
}

package com.joakibj.tswrdb.rdb

import java.io.{FileInputStream, BufferedInputStream}
import java.nio.{ByteOrder, ByteBuffer}

abstract class RdbFileReader {
  protected val MagicNumber: String
  protected val fileInputStream: FileInputStream

  val littleEndianInt = ByteBuffer.wrap(_: Array[Byte]).order(ByteOrder.LITTLE_ENDIAN).getInt
  val littleEndianByte = ByteBuffer.wrap(_: Array[Byte]).order(ByteOrder.LITTLE_ENDIAN).get
  val littleEndianArray = ByteBuffer.wrap(_: Array[Byte]).order(ByteOrder.LITTLE_ENDIAN).array

  def hasMagicNumber(): Boolean = {
    val buf: Array[Byte] = new Array(MagicNumber.size)
    fileInputStream.read(buf, 0, MagicNumber.length)
    if (new String(buf) == MagicNumber) true else false
  }
}

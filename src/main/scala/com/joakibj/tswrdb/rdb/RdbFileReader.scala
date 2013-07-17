package com.joakibj.tswrdb.rdb

import java.io.{FileInputStream, BufferedInputStream}
import java.nio.{ByteOrder, ByteBuffer}

abstract class RdbFileReader {
  protected val MagicNumber: String
  protected val fileInputStream: FileInputStream
  protected val bufferedInputStream: BufferedInputStream

  val littleEndianInt = ByteBuffer.wrap(_: Array[Byte]).order(ByteOrder.LITTLE_ENDIAN).getInt
  val littleEndianByte = ByteBuffer.wrap(_: Array[Byte]).order(ByteOrder.LITTLE_ENDIAN).get
  val littleEndianArray = ByteBuffer.wrap(_: Array[Byte]).order(ByteOrder.LITTLE_ENDIAN).array

  def hasMagicNumber(): Boolean = {
    bufferedInputStream.mark(MagicNumber.length)
    val buf: Array[Byte] = new Array(MagicNumber.size)
    bufferedInputStream.read(buf, 0, MagicNumber.length)
    bufferedInputStream.reset()
    if (new String(buf) == MagicNumber) true else false
  }
}

/*
 * tswrdb - a program and API to export the TSW resource database
 *
 * Copyright (C) 2013 Joakim Bjørnstad <joakibj@gmail.com>
 *
 * Licensed under the GNU General Public License version 2.
 * Please see the LICENSE file for the license text in verbatim.
 */

package com.joakibj.tswrdb.rdb.util

import java.nio.{ByteOrder, ByteBuffer}

trait ByteUtils {
  def littleEndianShort(buf: Array[Byte]): Short =
    ByteBuffer.wrap(buf).order(ByteOrder.LITTLE_ENDIAN).getShort

  def littleEndianInt(buf: Array[Byte]): Int =
    ByteBuffer.wrap(buf).order(ByteOrder.LITTLE_ENDIAN).getInt

  def littleEndianByte(buf: Array[Byte]): Byte =
    ByteBuffer.wrap(buf).order(ByteOrder.LITTLE_ENDIAN).get

  def littleEndianArray(buf: Array[Byte]): Array[Byte] =
    ByteBuffer.wrap(buf).order(ByteOrder.LITTLE_ENDIAN).array

  def intToBytes(i: Int): Array[Byte] =
    ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(i).array

  def byteToBytes(b: Byte): Array[Byte] = Array(b)

  def padding(num: Int): Array[Byte] = Array.fill(num)(0.toByte)

  def toHex(buffer: Array[Byte]): String = buffer.map("%02x" format _).mkString
}

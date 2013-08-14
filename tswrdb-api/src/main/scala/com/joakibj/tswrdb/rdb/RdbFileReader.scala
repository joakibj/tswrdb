/*
 * tswrdb - a program and API to export the TSW resource database
 *
 * Copyright (C) 2013 Joakim Bjørnstad <joakibj@gmail.com>
 *
 * Licensed under the GNU General Public License version 2.
 * Please see the LICENSE file for the license text in verbatim.
 */

package com.joakibj.tswrdb.rdb

import java.io.InputStream
import util.ByteUtils

abstract class RdbFileReader extends ByteUtils {
  protected val MagicNumber: String
  protected val inputStream: InputStream

  def close() {
    inputStream.close()
  }

  def hasMagicNumber(): Boolean = {
    val buf: Array[Byte] = new Array(MagicNumber.size)
    inputStream.read(buf)

    new String(buf) == MagicNumber
  }

  final def readInt(): Int = {
    val buf = new Array[Byte](4)
    inputStream.read(buf)
    val intVal = littleEndianInt(buf)

    intVal
  }

  final def readString() = {
    val len = readInt()

    val buf = new Array[Byte](len)
    inputStream.read(buf)
    val str = new String(buf)

    str
  }
}

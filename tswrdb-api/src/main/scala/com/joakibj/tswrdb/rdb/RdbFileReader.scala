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

  protected final def readByte(): Byte = {
    littleEndianByte(readLen(1))
  }

  protected final def readInt(): Int = {
    littleEndianInt(readLen(4))
  }

  protected final def readString(len: Int): String = {
    new String(readLen(len))
  }

  protected final def readHash(): String = {
    toHex(littleEndianArray(readLen(16)))
  }

  protected final def readLen(len: Int): Array[Byte] = {
    val buf = new Array[Byte](len)
    if(inputStream.read(buf) != -1) {
      buf
    } else {
      throw new RdbIOException("Prematurely got to end of file. No more data.", Severity.Mayan)
    }
  }
}

/*
 * tswrdb - a program and API to export the TSW resource database
 *
 * Copyright (C) 2013 Joakim Bjørnstad <joakibj@gmail.com>
 *
 * Licensed under the GNU General Public License version 2.
 * Please see the LICENSE file for the license text in verbatim.
 */

package com.joakibj.tswrdb.rdb.strings

import com.joakibj.tswrdb.rdb.RdbDataTransformer

class StringRdbDataTransformer extends RdbDataTransformer {
  def transform(buf: Array[Byte]): Array[Byte] = {
    val stringRdbReader = new RdbStringFileReader(buf)
    val strings = stringRdbReader.getStrings()

    strings.map {
      case (index, arr) =>
        index + ";" + new String(arr)
    }.mkString("\n").map(_.toByte).toArray
  }
}

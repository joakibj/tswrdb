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
import xml.PrettyPrinter

abstract class RdbString {
  val stringId: Int
  val content: String

  def toXml = {
    <rdbString>
      <stringId>{stringId}</stringId>
      <content>{content}</content>
    </rdbString>
  }
}

class StringRdbDataXmlTransformer extends RdbDataTransformer {
  def transform(buf: Array[Byte]): Array[Byte] = {
    val stringRdbReader = new RdbStringFileReader(buf)
    val strings = stringRdbReader.getStrings()
    if (strings.length == 0) return new Array[Byte](0)

    val xmlResult =
      <rdbStrings>
        {strings.map {
        case (id, str) =>
          new RdbString {
            val stringId = id
            val content = str
          }.toXml
      }}
      </rdbStrings>
    val printer = new PrettyPrinter(120, 2)
    printer.format(xmlResult).map(_.toByte).toArray
  }
}

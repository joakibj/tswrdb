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
import com.fasterxml.jackson.databind.{SerializationFeature, ObjectMapper}
import com.fasterxml.jackson.module.scala.DefaultScalaModule

class RdbStringDataJsonTransformer extends RdbDataTransformer {
  def transform(buf: Array[Byte]): Array[Byte] = {
    val stringRdbReader = new RdbStringFileReader(buf)
    val (_, strings) = stringRdbReader.getStrings()

    if (strings.length == 0) return new Array[Byte](0)

    val mapper = new ObjectMapper()
    mapper.registerModule(DefaultScalaModule)
    mapper.configure(SerializationFeature.INDENT_OUTPUT, true)

    mapper.writeValueAsBytes(strings)
  }
}

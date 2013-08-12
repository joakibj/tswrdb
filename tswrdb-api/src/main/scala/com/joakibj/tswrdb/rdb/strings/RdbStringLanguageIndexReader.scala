/*
 * tswrdb - a program and API to export the TSW resource database
 *
 * Copyright (C) 2013 Joakim Bjørnstad <joakibj@gmail.com>
 *
 * Licensed under the GNU General Public License version 2.
 * Please see the LICENSE file for the license text in verbatim.
 */

package com.joakibj.tswrdb.rdb.strings

import com.joakibj.tswrdb.rdb.RdbFileReader
import java.io.{FileInputStream, File, InputStream}

case class RdbStringCategory(rdbId: Int,
                             categoryId: Int,
                             categoryNamePair: (String, String))

object StringLanguage extends Enumeration {
  val English = LangVal("en", 55)
  val French = LangVal("fr", 56)
  val German = LangVal("de", 54)

  case class LangVal(name: String, headerLen: Int) extends Val(name)

  implicit def valueToLang(v: Value): LangVal = v.asInstanceOf[LangVal]
}

class RdbStringLanguageIndexReader(languageFile: File,
                                   lang: StringLanguage.Value) extends RdbFileReader {
  val MagicNumber: String = "TDL1"
  val inputStream: InputStream = new FileInputStream(languageFile)

  require(hasMagicNumber(), "Buf does not have the required MagicNumber and is not a language file")

  def readEntries() = {
    skipLanguageDependentHeader()

    val numEntries = readInt

    val rdbIdCategoryPairs =
      for {i <- 0 until numEntries} yield (readInt, readInt)

    inputStream.skip(2)

    val numCategoryPairs = readInt

    val categoryNamesTriplet =
      for {i <- 0 until numCategoryPairs} yield (readInt, readTextEntry, readTextEntry)

    inputStream.close()

    val seq =
      for {
        pair <- rdbIdCategoryPairs
        triplet <- categoryNamesTriplet
        if pair._1 == triplet._1
      } yield new RdbStringCategory(pair._2, pair._1, (triplet._2, triplet._3))

    seq.toList
  }

  private def readTextEntry = {
    val len = readInt

    val buf = new Array[Byte](len)
    inputStream.read(buf)
    val str = new String(buf)

    str
  }

  private def skipLanguageDependentHeader() {
    inputStream.skip(lang.headerLen)
  }

  private def readInt = {
    val buf = new Array[Byte](4)
    inputStream.read(buf)
    val intVal = littleEndianInt(buf)

    intVal
  }
}

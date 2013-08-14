/*
 * tswrdb - a program and API to export the TSW resource database
 *
 * Copyright (C) 2013 Joakim Bjørnstad <joakibj@gmail.com>
 *
 * Licensed under the GNU General Public License version 2.
 * Please see the LICENSE file for the license text in verbatim.
 */

package com.joakibj.tswrdb.rdb.strings

import com.joakibj.tswrdb.rdb.{RdbIOException, RdbFileReader}
import java.io.{FileInputStream, File, InputStream}
import scala.collection.mutable

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

    val numEntries = readInt()

    val rdbIdCategoryPairs =
      for {i <- 0 until numEntries} yield (readInt(), readInt())

    val entries = mutable.ListBuffer[RdbStringLangIndexEntry]()
    for (rdbCat <- rdbIdCategoryPairs) RdbStringCategories.find(rdbCat._1) match {
      case Some(cat) =>
        val entry = RdbStringLangIndexEntry(rdbCat._2, cat)
        entries += entry
      case None => throw new RdbIOException("category: " + rdbCat._1 + " was not found ")
    }

    entries.toList
  }

  private def skipLanguageDependentHeader() {
    inputStream.skip(lang.headerLen)
  }
}

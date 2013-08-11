/*
 * tswrdb - a program and API to export the TSW resource database
 *
 * Copyright (C) 2013 Joakim Bjørnstad <joakibj@gmail.com>
 *
 * Licensed under the GNU General Public License version 2.
 * Please see the LICENSE file for the license text in verbatim.
 */

package com.joakibj.tswrdb.rdb.strings

import java.io.File
import com.joakibj.tswrdb.rdb.export.RdbDataExporter

object RdbStringDataExporter {
  def apply(rdbFilename: String) =
    new RdbStringDataExporter(new File(rdbFilename))

  def apply(rdbDataDirectory: File) =
    new RdbStringDataExporter(rdbDataDirectory)
}

class RdbStringDataExporter(rdbDataDirectory: File) extends RdbDataExporter(rdbDataDirectory) {
  val postDataTransformer = new StringRdbDataXmlTransformer
}
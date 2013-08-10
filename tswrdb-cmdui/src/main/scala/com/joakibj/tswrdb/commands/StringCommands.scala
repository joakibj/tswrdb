/*
 * tswrdb - a program and API to export the TSW resource database
 *
 * Copyright (C) 2013 Joakim Bjørnstad <joakibj@gmail.com>
 *
 * Licensed under the GNU General Public License version 2.
 * Please see the LICENSE file for the license text in verbatim.
 */

package com.joakibj.tswrdb.commands

import com.joakibj.tswrdb.Config
import java.io.{FileInputStream, File}
import com.joakibj.tswrdb.rdb.strings.RdbStringFileReader

object StringCommands {
  val default = new StringExportCommand

  class StringExportCommand extends Command with ExitCommands {
    def execute(config: Config) {
      val buf = new Array[Byte](479790)
      val f = new File("exported\\1030002 (Strings)\\295807.dat")
      val in = new FileInputStream(f)
      in.read(buf)
      in.close()

      val strr = new RdbStringFileReader(buf)
      strr.getStrings()
    }
  }
}

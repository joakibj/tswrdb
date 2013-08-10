/*
 * tswrdb - a program and API to export the TSW resource database
 *
 * Copyright (C) 2013 Joakim Bjørnstad <joakibj@gmail.com>
 *
 * Licensed under the GNU General Public License version 2.
 * Please see the LICENSE file for the license text in verbatim.
 */

package com.joakibj.tswrdb

import java.io.File
import rdb.util.ByteUtils

object ListRdbTypesMode extends Enumeration {
  val None, All, Understood = Value
}

case class Config(tswDirectory: File = new File("."),
                  rdbType: Int = 0,
                  command: String = "",
                  subCommand: String = "",
                  listMode: Enumeration#Value = ListRdbTypesMode.None)

object TswRdb extends App with ByteUtils {
  Console.setErr(Console.out)

  val parser = new scopt.OptionParser[Config]("tswrdb") {
    head("tswrdb", "0.1")
    opt[File]("tsw") required() valueName ("<directory>") action {
      (file, config) =>
        config.copy(tswDirectory = file)
    } text ("tsw points to the TSW install directory and is required.")
    note("")
    cmd("list") action {
      (_, config) =>
        config.copy(command = "list", listMode = ListRdbTypesMode.Understood)
    } text ("Lists the valid rdb types available. Per default and to keep the user sane, only well understood RdbTypes are listed.") children (
      opt[Unit]("all") abbr ("a") action {
        (_, config) =>
          config.copy(listMode = ListRdbTypesMode.All)
      } text ("List all rdbtypes, regardless. Note that some are highly mysterious and/or esoteric. You will have to make sense of them yourself")
      )
    note("")
    cmd("export") action {
      (_, config) =>
        config.copy(command = "export")
    } text ("Export entries belonging to this rdbtype") children (
      arg[Int]("<rdbType>") required() action {
        (rdbType, config) =>
          config.copy(rdbType = rdbType)
      } text ("rdbType of the data that is going to be exported.")
      )
    note("")
    cmd("strings") action {
      (_, config) =>
        config.copy(command = "strings")
    }
    note("")
    cmd("index") action {
      (_, config) =>
        config.copy(command = "index")
    } children (
      cmd("info") action {
        (_, config) =>
          config.copy(subCommand = "info")
      } text ("Show information about index file: version, hash, number of entries")
      )
    note("")
    help("help") text ("prints this usage text.")
    version("version") text("prints the version")
  }

  parser.parse(args, Config()) map {
    config =>
      CommandDispatcher(config).dispatch()
  } getOrElse {

  }
}

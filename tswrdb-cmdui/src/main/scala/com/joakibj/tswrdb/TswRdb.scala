/*
 * tswrdb - a program and API to export the TSW resource database
 *
 * Copyright (C) 2013 Joakim Bjørnstad <joakibj@gmail.com>
 *
 * Licensed under the GNU General Public License version 2.
 * Please see the LICENSE file for the license text in verbatim.
 */

package com.joakibj.tswrdb

import java.io.{PrintStream, File}
import rdb.util.ByteUtils

object ListRdbTypesMode extends Enumeration {
  val None, All, Understood = Value
}

case class Config(rdbDataDirectory: File = new File("."),
                  rdbType: Int = 0,
                  command: String = "",
                  subCommand: String = "",
                  listMode: Enumeration#Value = ListRdbTypesMode.None)

object TswRdb extends App with ByteUtils {
  Console.setErr(Console.out)

  val parser = new scopt.OptionParser[Config]("tswrdb") {
    head("tswrdb", "0.1")
    help("help") text ("prints this usage text.")
    version("version")
    note("Data is exported to the export folder.")
    cmd("list") action {
      (_, config) =>
        config.copy(command = "list", listMode = ListRdbTypesMode.Understood)
    } text ("Lists the valid rdb types available. Per default and to keep the user sane, only well understood RdbTypes are listed.") children (
      opt[Unit]("all") abbr ("a") action {
        (_, config) =>
          config.copy(listMode = ListRdbTypesMode.All)
      } text ("List all rdbtypes, regardless. Note that some are highly mysterious and/or esoteric. You will have to make sense of them yourself")
      )
    cmd("export") action {
      (_, config) =>
        config.copy(command = "export")
    } text ("Export entries belonging to this rdbtype") children(
      opt[File]('r', "rdb") required() valueName ("<directory>") action {
        (file, config) =>
          config.copy(rdbDataDirectory = file)
      } text ("rdb points to the directory that has RDB files and is required."),
      arg[Int]("<rdbType>") required() action {
        (rdbType, config) =>
          config.copy(rdbType = rdbType)
      } text ("rdbType of the data that is going to be exported.")
      )
    cmd("index") action {
      (_, config) =>
        config.copy(command = "index")
    } children (
      cmd("info") action {
        (_, config) =>
          config.copy(subCommand = "info")
      } text ("Show information about index file: version, hash, entries") children (
        opt[File]('r', "rdb") required() valueName ("<directory>") action {
          (file, config) =>
            config.copy(rdbDataDirectory = file)
        } text ("rdb points to the directory that has RDB files and is required.")
        )
      )
  }

  parser.parse(args, Config()) map {
    config =>
      CommandDispatcher(config).dispatch()
  } getOrElse {

  }
}

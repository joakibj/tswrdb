/*
 * tswrdb - a program and API to export the TSW resource database
 *
 * Copyright (C) 2013 Joakim Bjørnstad <joakibj@gmail.com>
 *
 * Licensed under the GNU General Public License version 2.
 * Please see the LICENSE file for the license text in verbatim.
 */

package com.joakibj.tswrdb

import commands._

object CommandDispatcher {
  def apply(config: Config) = new CommandDispatcher(config)
}

class CommandDispatcher(config: Config) extends ExitCommands {
  def dispatch() {
    config.command match {
      case "list" =>
        config.listMode match {
          case ListRdbTypesMode.All => execute(ListCommands.all)
          case ListRdbTypesMode.Understood => execute(ListCommands.wellUnderstood)
          case _ => exit()
        }
      case "export" => execute(ExportCommands.default)
      case "index" =>
        config.subCommand match {
          case "info" => execute(IndexCommands.indexInfo)
          case _ => usageAndExit()
        }
      case _ => usageAndExit()
    }
  }

  def execute(command: Command) {
    command.execute(config)
  }
}

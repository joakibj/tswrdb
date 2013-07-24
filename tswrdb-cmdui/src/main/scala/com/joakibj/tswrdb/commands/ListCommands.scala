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
import com.joakibj.tswrdb.rdb.{RdbType, RdbTypes}

object ListCommands {
  val all = new ListAllCommand
  val wellUnderstood = new ListWellUnderstoodCommand

  trait ListCommand extends ExitCommands {
    def listRdbTypes(rdbTypes: List[RdbType]) {
      println("RdbType list:")
      rdbTypes foreach {
        (rdbType) =>
          println("\t" + rdbType.id + "\t" + rdbType.name)
      }
      exit()
    }
  }

  class ListAllCommand extends Command with ListCommand {
    def execute(config: Config) {
      listRdbTypes(RdbTypes.values)
    }
  }

  class ListWellUnderstoodCommand extends Command with ListCommand {
    def execute(config: Config) {
      listRdbTypes(RdbTypes.values.filter(_.understood))
    }
  }

}

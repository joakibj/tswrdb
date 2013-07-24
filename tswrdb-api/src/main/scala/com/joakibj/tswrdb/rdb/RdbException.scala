/*
 * tswrdb - a program and API to export the TSW resource database
 *
 * Copyright (C) 2013 Joakim Bjørnstad <joakibj@gmail.com>
 *
 * Licensed under the GNU General Public License version 2.
 * Please see the LICENSE file for the license text in verbatim.
 */

package com.joakibj.tswrdb.rdb

object Severity extends Enumeration {
  val Mayan = Value // unrecoverable
  val Continuable = Value // able to continue processing whatever
}

class RdbException(msg: String) extends Exception(msg)
case class RdbTypeNotFoundException(msg: String) extends RdbException(msg)
case class RdbIOException(msg: String, severity: Enumeration#Value = Severity.Continuable) extends RdbException(msg)

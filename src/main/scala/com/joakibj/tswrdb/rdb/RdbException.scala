package com.joakibj.tswrdb.rdb

object Severity extends Enumeration {
  val Mayan = Value // unrecoverable
  val Continuable = Value // able to continue processing whatever
}

class RdbException(msg: String) extends Exception(msg)
case class RdbTypeNotFoundException(msg: String) extends RdbException(msg)
case class RdbIOException(msg: String, severity: Enumeration#Value = Severity.Continuable) extends RdbException(msg)

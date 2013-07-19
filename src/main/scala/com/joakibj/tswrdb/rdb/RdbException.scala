package com.joakibj.tswrdb.rdb

object Severity extends Enumeration {
  val Mayan = Value // unrecoverable
  val Continuable = Value // able to continue processing whatever
}

class RdbException(msg: String) extends Exception(msg)
class RdbTypeNotFoundException(msg: String) extends RdbException(msg)
class RdbIOException(msg: String, severity: Enumeration#Value = Severity.Continuable) extends RdbException(msg)

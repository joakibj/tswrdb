package com.joakibj.tswrdb

import java.io._
import rdb.RdbIndexFileReader
import scala.io.Source

object TswRdb {
  def main(args: Array[String]) {
    val directory = "D:\\Programs\\TSW TestLive\\RDB"
    val indexReader = RdbIndexFileReader(new File(directory + "\\le.idx"))
    def toHex(buffer: Array[Byte]): String = buffer.map("%02X" format _).mkString
    indexReader.readIndexEntries

  }
}

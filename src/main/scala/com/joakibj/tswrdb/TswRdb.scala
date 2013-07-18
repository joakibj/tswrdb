package com.joakibj.tswrdb

import java.io._
import rdb.{RdbDataExporter, RdbIndexEntry, RdbIndexFileReader}

object TswRdb {
  def main(args: Array[String]) {
    val directory = "D:\\Programs\\TSW TestLive\\RDB"
    val indexReader = RdbIndexFileReader(new File(directory + "\\le.idx"))
    def toHex(buffer: Array[Byte]): String = buffer.map("%02X" format _).mkString

    val indexTable = indexReader.getIndexTable
    println("Found: " + indexTable.length + " index entries")

    RdbDataExporter.exportAll(new File(directory), indexTable.entriesForType(1010042))
  }
}

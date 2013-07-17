package com.joakibj.tswrdb

import java.io._
import rdb.{RdbDataFileReader, RdbIndexEntry, RdbIndexFileReader}
import scala.io.Source

object TswRdb {
  def main(args: Array[String]) {
    val directory = "D:\\Programs\\TSW TestLive\\RDB"
    val indexReader = RdbIndexFileReader(new File(directory + "\\le.idx"))
    def toHex(buffer: Array[Byte]): String = buffer.map("%02X" format _).mkString

    val indexEntries = indexReader.readIndexEntries
    println("Found: " + indexEntries.length + " index entries")
    val icons = indexEntries.filter((indexEntry: RdbIndexEntry) => indexEntry.rdbType == 1010008 && indexEntry.fileNum == 4)

    val dataReader = RdbDataFileReader(new File(directory + "\\04.rdbdata"), icons.toArray)
  }
}

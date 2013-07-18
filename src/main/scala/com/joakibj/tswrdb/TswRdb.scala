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
    println(indexEntries.filter((indexEntry: RdbIndexEntry) => indexEntry.rdbType == 1000520))
    //val icons = indexEntries.filter((indexEntry: RdbIndexEntry) => indexEntry.rdbType == 1010008 && indexEntry.fileNum == 4)

    //val dataReader = RdbDataFileReader(new File(directory + "\\04.rdbdata"), icons.toArray)

    val icons2 = indexEntries.filter((indexEntry: RdbIndexEntry) => indexEntry.rdbType == 1000520 && indexEntry.fileNum == 13)

    val dataReader = RdbDataFileReader(new File(directory + "\\13.rdbdata"), icons2.toArray)
    dataReader.readDataEntries()
  }
}

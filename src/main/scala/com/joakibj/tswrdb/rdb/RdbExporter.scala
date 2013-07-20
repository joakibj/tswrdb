package com.joakibj.tswrdb.rdb

import java.io.File

object RdbExporter {
  def apply(rdbFilename: String) = new RdbExporter(new File(rdbFilename))
  def apply(rdbDataDirectory: File) = new RdbExporter(rdbDataDirectory)
}

class RdbExporter(val rdbDataDirectory: File) {
  val IndexFilename = "le.idx"
  val validRdbFileNums = rdbDataDirectory.listFiles.filter(_.getName.endsWith(".rdbdata")).map(_.getName.split("\\.").head.toInt)
  val indexTable = RdbIndexFileReader(new File(rdbDataDirectory, IndexFilename)).getIndexTable

  def exportAll(rdbType: Int) {
    val groupedIndexEntries = grouped(indexTable.entriesForType(rdbType))
    groupedIndexEntries.keys.foreach {
      (fileNum) =>
        try {
          exportEntriesFromFileNum(fileNum, groupedIndexEntries(fileNum))
        } catch {
          case ex: RdbIOException => ex match {
            case RdbIOException(msg @ _, Severity.Continuable) => {
              println("Recoverable exception occured: " + msg + ". Continuing...")
            }
            case RdbIOException(msg @ _, Severity.Mayan) => {
              throw new RuntimeException("Unrecoverable exception occured: " + msg)
            }
          }
          case _: Throwable => throw new RuntimeException("Unknown unrecoverable exception occured")
        }
    }
  }

  private def exportEntriesFromFileNum(fileNum: Int, indexEntries: Array[RdbIndexEntry]) {
    if(!validRdbFileNums.contains(fileNum)) throw new RdbIOException("Filenum: " + fileNum + " does not exist")

    val rdbDataFile = new File(rdbDataDirectory, "%02d.rdbdata" format fileNum)
    val dataExporter = RdbDataFileExporter(rdbDataFile, indexEntries)
    println("Exporting entries from: " + rdbDataFile.getName)
    dataExporter.exportDataEntries()
  }

  private def grouped(arr: Array[RdbIndexEntry]): Map[Int, Array[RdbIndexEntry]] =
    arr.groupBy((indexEntry: RdbIndexEntry) => indexEntry.fileNum.toInt)
}

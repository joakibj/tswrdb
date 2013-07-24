/*
 * tswrdb - a program and API to export the TSW resource database
 *
 * Copyright (C) 2013 Joakim Bjørnstad <joakibj@gmail.com>
 *
 * Licensed under the GNU General Public License version 2.
 * Please see the LICENSE file for the license text in verbatim.
 */

package com.joakibj.tswrdb.rdb.export

import java.io.File
import com.joakibj.tswrdb.rdb.index.{RdbIndexEntry, RdbIndexFileReader}
import com.joakibj.tswrdb.rdb.{Severity, RdbIOException}

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
    val outputDirectory = createOutputDirectory(groupedIndexEntries.size, rdbType).getOrElse {
      throw new RuntimeException("Unable to create export directory.")
    }

    groupedIndexEntries.keys.foreach {
      (fileNum) =>
        try {
          exportEntriesFromFileNum(outputDirectory, fileNum, groupedIndexEntries(fileNum))
        } catch {
          case ex: RdbIOException => ex match {
            case RdbIOException(msg@_, Severity.Continuable) => {
              println("Recoverable exception occured: " + msg + ". Continuing...")
            }
            case RdbIOException(msg@_, Severity.Mayan) => {
              throw new RuntimeException("Unrecoverable exception occured: " + msg)
            }
          }
          case ex: Throwable => throw new RuntimeException("Unknown unrecoverable exception occured: "
            + ex.getClass.getName + ": " + ex.getMessage)
        }
    }
  }

  private def createOutputDirectory(entries: Int, rdbType: Int): Option[File] = {
    val outputDirectory = new File("./exported/" + rdbType)
    val created = if (!outputDirectory.isDirectory) outputDirectory.mkdirs() else true

    if (created) Some(outputDirectory) else None
  }

  private def exportEntriesFromFileNum(outputDirectory: File, fileNum: Int, indexEntries: Array[RdbIndexEntry]) {
    if (!validRdbFileNums.contains(fileNum)) throw new RdbIOException("Filenum: " + fileNum + " does not exist")

    val rdbDataFile = new File(rdbDataDirectory, "%02d.rdbdata" format fileNum)
    val dataExporter = RdbDataFileExporter(outputDirectory, rdbDataFile, indexEntries)
    println("Exporting entries from: " + rdbDataFile.getName)
    dataExporter.exportDataEntries()
  }

  private def grouped(arr: Array[RdbIndexEntry]): Map[Int, Array[RdbIndexEntry]] =
    arr.groupBy((indexEntry: RdbIndexEntry) => indexEntry.fileNum.toInt)
}

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
import com.joakibj.tswrdb.rdb._
import com.joakibj.tswrdb.rdb.RdbIOException
import scala.Some

object RdbExporter {
  def apply(rdbFilename: String) = new RdbExporter(new File(rdbFilename))

  def apply(rdbDataDirectory: File) = new RdbExporter(rdbDataDirectory)

  def apply(rdbDataDirectory: File, postDataTransformer: RdbDataTransformer) = new RdbExporter(rdbDataDirectory, postDataTransformer)
}

class RdbExporter(val rdbDataDirectory: File,
                  postDataTransformer: RdbDataTransformer = new NoRdbDataTransformer) {
  val IndexFilename = "le.idx"
  val validRdbFileNums = rdbDataDirectory.listFiles.filter(_.getName.endsWith(".rdbdata")).map(_.getName.split("\\.").head.toInt)
  val indexTable = RdbIndexFileReader(new File(rdbDataDirectory, IndexFilename)).getIndexTable

  def exportAll(rdbType: RdbType) {
    val groupedIndexEntries = grouped(indexTable.entriesForType(rdbType.id))
    val outputDirectory = createOutputDirectory(groupedIndexEntries.size, rdbType).getOrElse {
      throw new RuntimeException("Unable to create exported directory.")
    }

    groupedIndexEntries.keys.foreach {
      (fileNum) =>
        try {
          exportEntriesFromFileNum(rdbType, outputDirectory, fileNum, groupedIndexEntries(fileNum))
        } catch {
          case ex: RdbIOException => ex match {
            case RdbIOException(msg@_, Severity.Continuable) => {
              println("Recoverable exception occured: " + msg + ". Continuing...")
            }
            case RdbIOException(msg@_, Severity.Mayan) => {
              throw new RuntimeException("Unrecoverable exception occured: " + msg)
            }
          }
          case ex: Throwable =>
            throw new RuntimeException("Unknown unrecoverable exception occured: " + ex.getClass.getName + ": " + ex.getMessage)
        }
    }
  }

  private def createOutputDirectory(entries: Int, rdbType: RdbType): Option[File] = {
    val outputDirectory = new File("./exported/" + rdbType)
    val created = if (!outputDirectory.isDirectory) outputDirectory.mkdirs() else true

    if (created) Some(outputDirectory) else None
  }

  private def exportEntriesFromFileNum(rdbType: RdbType, outputDirectory: File, fileNum: Int, indexEntries: Array[RdbIndexEntry]) {
    if (!validRdbFileNums.contains(fileNum)) throw new RdbIOException("Filenum: " + fileNum + " does not exist")

    val rdbDataFile = new File(rdbDataDirectory, "%02d.rdbdata" format fileNum)
    val rdbDataFileReader = RdbDataFileReader(rdbDataFile, indexEntries)
    val rdbData = rdbDataFileReader.readDataEntries()
    exportData(rdbType, outputDirectory, rdbData)

    println("Exporting entries from: " + rdbDataFile.getName)
  }

  private def exportData(rdbType: RdbType, outputDirectory: File, rdbData: Vector[(RdbDataEntry, Array[Byte])]) {
    rdbData.foreach {
      case (entry, buf) =>
        val transformedBuf = postDataTransformer.transform(buf)
        if(transformedBuf.size > 0) {
          val filename = entry.id + "." + rdbType.fileType.extension
          val fileWriter = DataFileWriter(new File(outputDirectory, filename))
          fileWriter.writeData(transformedBuf)
        } else throw new RdbIOException("Entry " + entry.id + " was empty. Skipped write.")
    }
  }

  private def grouped(arr: Array[RdbIndexEntry]): Map[Int, Array[RdbIndexEntry]] =
    arr.groupBy((indexEntry: RdbIndexEntry) => indexEntry.fileNum.toInt)
}

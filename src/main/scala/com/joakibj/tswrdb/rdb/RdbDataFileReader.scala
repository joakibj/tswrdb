package com.joakibj.tswrdb.rdb

import java.io.{BufferedInputStream, FileInputStream, File}

class RdbDataEntry(val rdbType: Int, val id: Int, val length: Int, val buf: Array[Byte] = Array())

object RdbDataFileReader {
  def apply(file: File, indexEntries: Array[RdbIndexEntry]) = new RdbDataFileReader(file, indexEntries)
}

class RdbDataFileReader(file: File, ie: Array[RdbIndexEntry]) extends RdbFileReader {
  require(file.isFile, "Datafile does not exist")

  val MagicNumber: String = "RDB0"
  val fileInputStream = new FileInputStream(file)
  val bufferedInputStream = new BufferedInputStream(fileInputStream)

  require(validIndexEntries(ie), "All index entries must be in file: " + file.getName)

  private val indexEntries = sortedEntries(ie)

  require(hasMagicNumber(), "Datafile does not have the magic number")

  private def sortedEntries(indexEntries: Array[RdbIndexEntry]): Array[RdbIndexEntry] = indexEntries.sortBy(_.dataOffset)

  private def validIndexEntries(indexEntries: Array[RdbIndexEntry]): Boolean =
    indexEntries.count((indexEntry: RdbIndexEntry) => indexEntry.fileName == file.getName) == indexEntries.size

  def readData() {
    bufferedInputStream.skip(4)


  }

  private def readDataEntry(skipBytes: Int, indexEntry: RdbIndexEntry) {
    bufferedInputStream.skip(skipBytes)

    val buf: Array[Byte] = new Array(16 + indexEntry.length)
    bufferedInputStream.read(buf, 0, indexEntry.length)



    if(!isCorrectDataEntry()) throw new RuntimeException("Wrong dataentry encountered for id: " + indexEntry.id)

  }

  private def isCorrectDataEntry(): Boolean = {
    false
  }
}

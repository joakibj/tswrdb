package com.joakibj.tswrdb.rdb.strings

import com.joakibj.tswrdb.rdb._
import java.io.{ByteArrayInputStream, InputStream}
import com.joakibj.tswrdb.rdb.RdbIOException
import scala.collection.mutable

case class RdbFilename(rdbId: Int, fileName: String)

object RdbFilenameReader {
  def apply(buf: Array[Byte]) = new RdbFilenameReader(buf)
}

class RdbFilenameReader(buf: Array[Byte]) extends RdbFileReader {
  val MagicNumber: String = ""
  val inputStream: InputStream = new ByteArrayInputStream(buf)

  def getFileNames() = {
    val filenames = mutable.Map.empty[RdbType, Vector[RdbFilename]]

    val numTypes = readInt()

    for (rdbTypeNum <- 0 until numTypes) {
      val rdbTypez = readInt()
      val numEntries = readInt()

      if (numEntries != 0) {
        val rdbType = RdbTypes.find(rdbTypez).getOrElse {
          throw new RdbIOException("Unable to find RdbType " + rdbTypez)
        }
        val filenameEntries =
          for {
            entryNum <- 0 until numEntries
          } yield readFilenameEntry()
        filenames += (rdbType -> filenameEntries.toVector)
      }
    }
    filenames
  }

  private def readFilenameEntry() = {
    val rdbId = readInt()
    val filenameLen = readInt()
    val filename = readString(filenameLen)

    RdbFilename(rdbId, filename)
  }
}

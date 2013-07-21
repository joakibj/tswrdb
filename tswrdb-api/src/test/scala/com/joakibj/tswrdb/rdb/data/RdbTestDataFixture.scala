package com.joakibj.tswrdb.rdb.data

import com.joakibj.tswrdb.rdb.export.RdbDataEntry
import com.joakibj.tswrdb.rdb.util.ByteUtils
import scala.Array


object RdbTestDataFixture extends ByteUtils {
  val MagicNumber: Array[Byte] = "RDB0" map (_.toByte) toArray

  def generateTestDataFile1: Array[Byte] = {
    MagicNumber ++
      generateEntry(RdbDataEntry(1000001, 1, 15, new String("IHateMayansSoBd").toArray.map(_.toByte))) ++
      generateEntry(RdbDataEntry(1000001, 2, 15, new String("IHateMayansSoBd").toArray.map(_.toByte))) ++
      generateEntry(RdbDataEntry(1000001, 3, 15, new String("IHateMayansSoBd").toArray.map(_.toByte))) ++
      generateEntry(RdbDataEntry(1000001, 4, 15, new String("IHateMayansSoBd").toArray.map(_.toByte))) ++
      generateEntry(RdbDataEntry(1000001, 5, 15, new String("IHateMayansSoBd").toArray.map(_.toByte)))
  }

  def generateTestDataFile2: Array[Byte] = {
    MagicNumber ++
      generateEntry(RdbDataEntry(1000005, 6, 10, new String("abcdeABCDE").toArray.map(_.toByte))) ++
      generateEntry(RdbDataEntry(1000005, 7, 10, new String("abcdeABCDE").toArray.map(_.toByte))) ++
      generateEntry(RdbDataEntry(1000005, 8, 10, new String("abcdeABCDE").toArray.map(_.toByte))) ++
      generateEntry(RdbDataEntry(1000005, 9, 10, new String("abcdeABCDE").toArray.map(_.toByte))) ++
      generateEntry(RdbDataEntry(1000005, 10, 10, new String("abcdeABCDE").toArray.map(_.toByte)))
  }

  def generateEntry(entry: RdbDataEntry): Array[Byte] = {
    entry.toArray
  }
}

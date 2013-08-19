package com.joakibj.tswrdb.rdb.data

import com.joakibj.tswrdb.rdb.export.RdbDataEntry
import com.joakibj.tswrdb.rdb.util.ByteUtils
import scala.Array


object RdbTestDataFixture extends ByteUtils {
  val MagicNumber = "RDB0".getBytes

  def generateTestDataFile1: Array[Byte] = {
    val buf = new String("IHateMayansSoBd").getBytes
    MagicNumber ++
      RdbDataEntry(1000001, 1, 15, buf).toArray ++
      RdbDataEntry(1000001, 2, 15, buf).toArray ++
      RdbDataEntry(1000001, 3, 15, buf).toArray ++
      RdbDataEntry(1000001, 4, 15, buf).toArray ++
      RdbDataEntry(1000001, 5, 15, buf).toArray
  }

  def generateTestDataFile2: Array[Byte] = {
    val buf = new String("abcdeABCDE").getBytes
    MagicNumber ++
      RdbDataEntry(1000005, 6, 10, buf).toArray ++
      RdbDataEntry(1000005, 7, 10, buf).toArray ++
      RdbDataEntry(1000005, 8, 10, buf).toArray ++
      RdbDataEntry(1000005, 9, 10, buf).toArray ++
      RdbDataEntry(1000005, 10, 10, buf).toArray
  }
}

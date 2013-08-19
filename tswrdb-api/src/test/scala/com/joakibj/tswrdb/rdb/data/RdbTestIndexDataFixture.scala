package com.joakibj.tswrdb.rdb.data

import collection.mutable.ArrayBuffer
import com.joakibj.tswrdb.rdb.index.{RdbIndexHeader, RdbIndexEntry}
import com.joakibj.tswrdb.rdb.util.ByteUtils

object RdbTestIndexDataFixture extends ByteUtils {
  val MagicNumber = "IBDR".getBytes   
  val DummyHash = Array.fill(16)(0.toByte)

  def indexfile = {
    header ++ 
    indexTable ++
    indexEntries
  }

  def entries = 
    List(
      RdbIndexEntry(100000, 1, 1, 0, 15, DummyHash),
      RdbIndexEntry(100000, 2, 1, 15, 15, DummyHash),
      RdbIndexEntry(100000, 3, 1, 30, 15, DummyHash),
      RdbIndexEntry(100000, 4, 1, 45, 15, DummyHash),
      RdbIndexEntry(100000, 5, 1, 60, 15, DummyHash),
      RdbIndexEntry(100001, 6, 2, 75, 10, DummyHash),
      RdbIndexEntry(100001, 7, 2, 85, 10, DummyHash),
      RdbIndexEntry(100001, 8, 2, 95, 10, DummyHash),
      RdbIndexEntry(100001, 9, 2, 105, 10, DummyHash),
      RdbIndexEntry(100001, 10, 2, 115, 10, DummyHash)
    )

  def header = MagicNumber ++ RdbIndexHeader(7, DummyHash, 10).toArray
    
  def indexTable = {
    val itable =
      for {
        ie <- entries
      } yield ie.toArray.take(8)
    
    itable.toArray.flatten
  }

  def indexEntries = {
    val details =
      for {
        ie <- entries
      } yield ie.toArray.drop(8)

    details.toArray.flatten
  }
}

package com.joakibj.tswrdb.rdb

import scala.collection.mutable.ArrayBuffer
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest._

@RunWith(classOf[JUnitRunner])
class RdbDataIndexTableTest extends FunSuite with ShouldMatchers {
  test("should return length of table") {
    val indexTable = RdbDataIndexTable(testData)
    
    indexTable.length should be(5)
  }

  test("should return set with unique ") {
    val indexTable = RdbDataIndexTable(testData)

    indexTable.types should equal(Set(1000001, 1000005))
  }

  test("should return entries for a RdbType id, grouped by filenum") {
    val indexTable = RdbDataIndexTable(testData)

    val entries = indexTable.entriesForType(1000001)

    entries should (contain key(1) and contain key(2))
    entries(1) should have size 1
    entries(2) should have size 2
  }

  private def testData = {
    val buf = ArrayBuffer[RdbIndexEntry]()
    val DummyHash: Array[Byte] = Array.fill(16)(0)
    buf += RdbIndexEntry(1000001, 1, 1, 0, 10, DummyHash)
    buf += RdbIndexEntry(1000001, 2, 2, 10, 10, DummyHash)
    buf += RdbIndexEntry(1000001, 3, 2, 20, 10, DummyHash)
    buf += RdbIndexEntry(1000005, 4, 1, 30, 15, DummyHash)
    buf += RdbIndexEntry(1000005, 5, 1, 45, 15, DummyHash)

    buf
  }
}

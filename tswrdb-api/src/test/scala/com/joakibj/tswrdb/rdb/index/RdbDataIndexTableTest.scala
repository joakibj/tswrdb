package com.joakibj.tswrdb.rdb.index

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest._
import com.joakibj.tswrdb.rdb.data.RdbTestIndexDataFixture

@RunWith(classOf[JUnitRunner])
class RdbDataIndexTableTest extends FunSuite with ShouldMatchers {
  val DummyHash = RdbTestIndexDataFixture.DummyHash

  test("should return length of table") {
    val indexTable = RdbDataIndexTable(testHeader, testData)

    indexTable.length should be(5)
  }

  test("should return set with unique RdbTypes") {
    val indexTable = RdbDataIndexTable(testHeader, testData)

    indexTable.types should equal(Set(1000001, 1000005))
  }

  test("should return entries for a RdbType id") {
    val indexTable = RdbDataIndexTable(testHeader, testData)

    val entries = indexTable.entriesForType(1000001)

    entries should have size (3)
  }

  private def testData = {
    Vector(
      RdbIndexEntry(1000001, 1, 1, 0, 10, DummyHash),
      RdbIndexEntry(1000001, 2, 2, 10, 10, DummyHash),
      RdbIndexEntry(1000001, 3, 2, 20, 10, DummyHash),
      RdbIndexEntry(1000005, 4, 1, 30, 15, DummyHash),
      RdbIndexEntry(1000005, 5, 1, 45, 15, DummyHash)
    )
  }

  private def testHeader = {
    RdbIndexHeader(7, DummyHash, 5)
  }
}

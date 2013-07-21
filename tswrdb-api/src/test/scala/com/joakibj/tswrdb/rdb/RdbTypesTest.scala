package com.joakibj.tswrdb.rdb

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest._

@RunWith(classOf[JUnitRunner])
class RdbTypesTest extends FunSuite with ShouldMatchers {
  test("RdbType with the same id should be equal") {
    RdbType(1, "1", 0, FileType("dat")) should equal(RdbType(1, "2", 1, FileType("png")))
  }

  test("RdbType with different id should not be equal") {
    RdbType(1, "1", 0, FileType("dat")) should not equal(RdbType(2, "2", 1, FileType("png")))
  }

  test("should find RdbType by id") {
    val rdbType = RdbTypes.find(1000001)

    rdbType should be(Some(RdbType(1000001, "Map Info", 0, FileType("dat"))))
  }

  test("should return none if RdbType does not exist") {
    val rdbType = RdbTypes.find(9999999)

    rdbType should be(None)
  }

  test("should return true for RdbType that exists") {
    val result = RdbTypes.exists(1000001)

    result should be(true)
  }

  test("should return false for RdbType that does not exist") {
    val result = RdbTypes.exists(9999999)

    result should be(false)
  }

  test("should return list of RdbTypes") {
    val result = RdbTypes.values

    result.size should be(169)
  }
}

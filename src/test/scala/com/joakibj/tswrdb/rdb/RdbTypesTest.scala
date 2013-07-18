package com.joakibj.tswrdb.rdb

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest._

@RunWith(classOf[JUnitRunner])
class RdbTypesTest extends FunSuite with ShouldMatchers {
  test("should find RdbType by id") {
    val rdbType = RdbTypes.find(1000001)

    rdbType should not be(None)
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
}

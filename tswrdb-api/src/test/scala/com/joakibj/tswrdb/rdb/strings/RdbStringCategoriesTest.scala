/*
 * tswrdb - a program and API to export the TSW resource database
 *
 * Copyright (C) 2013 Joakim Bjørnstad <joakibj@gmail.com>
 *
 * Licensed under the GNU General Public License version 2.
 * Please see the LICENSE file for the license text in verbatim.
 */

package com.joakibj.tswrdb.rdb.strings

import org.scalatest.junit.JUnitRunner
import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers

@RunWith(classOf[JUnitRunner])
class RdbStringCategoriesTest extends FunSuite with ShouldMatchers {

  test("should find string category") {
    val stringCategory = RdbStringCategories.find(100)

    stringCategory should be(Some(RdbStringCategory(100, "gamecode")))
  }

  test("should not find category") {
    val stringCategory = RdbStringCategories.find(0)

    stringCategory should be(None)
  }
}

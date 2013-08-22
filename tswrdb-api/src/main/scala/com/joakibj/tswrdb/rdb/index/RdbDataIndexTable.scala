/*
 * tswrdb - a program and API to export the TSW resource database
 *
 * Copyright (C) 2013 Joakim Bjørnstad <joakibj@gmail.com>
 *
 * Licensed under the GNU General Public License version 2.
 * Please see the LICENSE file for the license text in verbatim.
 */

package com.joakibj.tswrdb.rdb.index

import com.joakibj.tswrdb.rdb.{RdbTypeNotFoundException, RdbTypes}
import com.joakibj.tswrdb.rdb.util.ByteUtils

case class RdbIndexHeader(version: Int,
                          hash: Array[Byte],
                          numEntries: Int) extends ByteUtils {

  def toArray: Array[Byte] =
    intToBytes(version) ++
      hash ++
      intToBytes(numEntries)
}

object RdbIndexEntry {
  def apply(tup2: Tuple2[Int, Int], tup4: Tuple4[Byte, Int, Int, Array[Byte]]) =
    new RdbIndexEntry(tup2._1, tup2._2, tup4._1, tup4._2, tup4._3, tup4._4)
}

case class RdbIndexEntry(rdbType: Int,
                         id: Int,
                         fileNum: Byte,
                         dataOffset: Int,
                         length: Int,
                         hash: Seq[Byte]) extends ByteUtils {
  val fileName = "%02d.rdbdata" format fileNum

  def toArray =
    intToBytes(rdbType) ++
      intToBytes(id) ++
      byteToBytes(fileNum) ++
      padding(3) ++
      intToBytes(dataOffset) ++
      intToBytes(length) ++
      hash
}

object RdbDataIndexTable {
  def apply(header: RdbIndexHeader, table: Vector[RdbIndexEntry]) =
    new RdbDataIndexTable(header, table)
}

class RdbDataIndexTable(private val header: RdbIndexHeader,
                        private val table: Vector[RdbIndexEntry]) {
  def length = table.size

  def types = table.map(_.rdbType).toSet

  def entriesForType(in: Int): Array[RdbIndexEntry] = {
    if (!RdbTypes.exists(in)) throw new RdbTypeNotFoundException("RdbType not found")

    table.filter((indexEntry: RdbIndexEntry) => indexEntry.rdbType == in).toArray
  }
}

package com.joakibj.tswrdb.rdb

import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.io.File
import java.io.FileOutputStream
import scala.collection.mutable.ArrayBuffer
import org.scalatest.FunSuite
import org.scalatest.ShouldMatchers
import org.scalatest.BeforeAndAfterAll
import org.scalatest.PrivateMethodTester._
import org.scalatest.junit.JUnitRunner
import org.junit.runner.RunWith

@RunWith(classOf[JUnitRunner])
class RdbIndexFileReaderTest extends FunSuite with BeforeAndAfterAll with ShouldMatchers {

  val DummyHash = Array.fill(16)(0.toByte)
  val tmpFile: File = File.createTempFile("test", "idx")

  override def beforeAll {
  	setupData()
  }

  test("should create object and validate magic word") {
  	val reader = RdbIndexFileReader(tmpFile)
  }

  test("should have read number of index entries in the header") {
  	val reader = RdbIndexFileReader(tmpFile)
  	assert(reader.numEntries === 10)	
  }

  test("should read next index table entry") {
    val reader = RdbIndexFileReader(tmpFile)
    reader.skipHeader()
    val readNextIndexEntry = PrivateMethod[(Int, Int)]('readNextIndexEntry)
    val result = reader invokePrivate readNextIndexEntry()

    assert(result._1 === 100000)
    assert(result._2 === 1)
  }

  test("should read next index entry details") {
    val reader = RdbIndexFileReader(tmpFile)
    reader.bufferedInputStream.skip(28 + (10 * 8))

    val readNextIndexEntryDetail = PrivateMethod[(Int, Int, Int, Array[Byte])]('readNextIndexEntryDetail)
    val result = reader invokePrivate readNextIndexEntryDetail()

    assert(result._1 === 1)
    assert(result._2 === 0)
    assert(result._3 === 15)
    assert(result._4 === DummyHash)
  }

  test("should read all index entries and return an array with each consolidated entry") {
    val reader = RdbIndexFileReader(tmpFile)
    val result = reader.readIndexEntries()

    ///TODO: this is an ugly hack, why doesn't deep object inspection happen? use should have on properties instead?
    result(0).toString should be(RdbIndexEntry(100000, 1, 1, 0, 15, DummyHash).toString)
    result(1).toString should be(RdbIndexEntry(100000, 2, 1, 15, 15, DummyHash).toString)
    result(2).toString should be(RdbIndexEntry(100000, 3, 1, 30, 15, DummyHash).toString)
    result(3).toString should be(RdbIndexEntry(100000, 4, 1, 45, 15, DummyHash).toString)
    result(4).toString should be(RdbIndexEntry(100000, 5, 1, 60, 15, DummyHash).toString)
    result(5).toString should be(RdbIndexEntry(100001, 6, 2, 75, 10, DummyHash).toString)
    result(6).toString should be(RdbIndexEntry(100001, 7, 2, 85, 10, DummyHash).toString)
    result(7).toString should be(RdbIndexEntry(100001, 8, 2, 95, 10, DummyHash).toString)
    result(8).toString should be(RdbIndexEntry(100001, 9, 2, 105, 10, DummyHash).toString)
    result(9).toString should be(RdbIndexEntry(100001, 10, 2, 115, 10, DummyHash).toString)
  }

  override def afterAll {
  	tmpFile.delete()
  }

  def setupData() {
  	val MagicNumber: Array[Byte] = "IBDR" map(_.toByte) toArray
  	val header: Array[Byte] =  MagicNumber ++ 
                              intToBytes(7) ++ 
                              padding(16) ++
                              intToBytes(10)

  	val testData: Array[Byte] = header ++ generateIndexTable(10) ++ generateIndexEntries(10)
  	val fos = new FileOutputStream(tmpFile)
  	fos.write(testData)
  	fos.close()
  }

  def intToBytes(i: Int): Array[Byte] = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(i).array
  def byteToBytes(b: Byte): Array[Byte] = Array(b)
  def padding(num: Int): Array[Byte] = Array.fill(num)(0.toByte)
  def toHex(buffer: Array[Byte]): String = buffer.map("%02X" format _).mkString

  def generateIndexTable(num: Int): Array[Byte] = {
  	val table: ArrayBuffer[Byte] = ArrayBuffer()

    (1 to num).foreach {
      (i) => 
        var entry = Array[Byte]()
        if(i <= 5)
          entry = intToBytes(100000) ++ 
                  intToBytes(i)
        else
          entry = intToBytes(100001) ++ 
                  intToBytes(i)
        table ++= entry
    }

    table.toArray
  }

  def generateIndexEntries(num: Int): Array[Byte] = {
    val entries: ArrayBuffer[Byte] = ArrayBuffer()

    (0 until num).foreach {
      (i) => 
        var entry = Array[Byte]()
        if(i < 5)
          entry = byteToBytes(1) ++ 
                  padding(3) ++ 
                  intToBytes(i * 15) ++ 
                  intToBytes(15) ++ 
                  DummyHash
        else
          entry = byteToBytes(2) ++ 
                  padding(3) ++ 
                  intToBytes((5 * 15) +  (i - 5) * 10) ++ 
                  intToBytes(10) ++ 
                  DummyHash
        entries ++= entry
    }

    entries.toArray
  }
}

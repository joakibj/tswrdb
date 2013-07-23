package com.joakibj.tswrdb.rdb.index

import java.io.File
import java.io.FileOutputStream
import scala.collection.mutable.ArrayBuffer
import org.scalatest.FunSuite
import org.scalatest.ShouldMatchers
import org.scalatest.BeforeAndAfterAll
import org.scalatest.PrivateMethodTester._
import org.scalatest.junit.JUnitRunner
import org.junit.runner.RunWith
import com.joakibj.tswrdb.rdb.data.RdbTestIndexDataFixture

@RunWith(classOf[JUnitRunner])
class RdbIndexFileReaderTest extends FunSuite with BeforeAndAfterAll with ShouldMatchers {

  val DummyHash = RdbTestIndexDataFixture.DummyHash
  val tmpFile: File = File.createTempFile("test", "idx")

  override def beforeAll {
  	setupData()
  }

  test("should create object and validate magic word") {
  	RdbIndexFileReader(tmpFile)
  }

  test("should fail to create object if it is not a file") {
    intercept[IllegalArgumentException] {
      RdbIndexFileReader(new File(System.getProperty("java.io.tmpdir")))
    }
  }

  test("should have read index header") {
  	val reader = RdbIndexFileReader(tmpFile)
  	val header = reader.indexHeader

    header.version should equal(7)
    header.hash should equal(DummyHash)
    header.numEntries should equal(10)
  }

  test("should read next index table entry") {
    val reader = RdbIndexFileReader(tmpFile)

    val readNextIndexEntry = PrivateMethod[(Int, Int)]('readNextIndexEntry)
    val result = reader invokePrivate readNextIndexEntry()

    assert(result._1 === 100000)
    assert(result._2 === 1)
  }

  test("should read next index entry details") {
    val reader = RdbIndexFileReader(tmpFile)
    reader.fileInputStream.skip(10 * 8)

    val readNextIndexEntryDetail = PrivateMethod[(Int, Int, Int, Array[Byte])]('readNextIndexEntryDetail)
    val result = reader invokePrivate readNextIndexEntryDetail()

    assert(result._1 === 1)
    assert(result._2 === 0)
    assert(result._3 === 15)
    assert(result._4 === DummyHash)
  }

  test("should read all index entries and return an array with each consolidated entry") {
    val reader = RdbIndexFileReader(tmpFile)

    val readIndexEntries = PrivateMethod[ArrayBuffer[RdbIndexEntry]]('readIndexEntries)
    val result = reader invokePrivate readIndexEntries()

    result(0) should equal(RdbIndexEntry(100000, 1, 1, 0, 15, DummyHash))
    result(1) should equal(RdbIndexEntry(100000, 2, 1, 15, 15, DummyHash))
    result(2) should equal(RdbIndexEntry(100000, 3, 1, 30, 15, DummyHash))
    result(3) should equal(RdbIndexEntry(100000, 4, 1, 45, 15, DummyHash))
    result(4) should equal(RdbIndexEntry(100000, 5, 1, 60, 15, DummyHash))
    result(5) should equal(RdbIndexEntry(100001, 6, 2, 75, 10, DummyHash))
    result(6) should equal(RdbIndexEntry(100001, 7, 2, 85, 10, DummyHash))
    result(7) should equal(RdbIndexEntry(100001, 8, 2, 95, 10, DummyHash))
    result(8) should equal(RdbIndexEntry(100001, 9, 2, 105, 10, DummyHash))
    result(9) should equal(RdbIndexEntry(100001, 10, 2, 115, 10, DummyHash))
  }

  test("should return a RdbDataIndexTable as part of the public API") {
    val reader = RdbIndexFileReader(tmpFile)

    val table: RdbDataIndexTable = reader.getIndexTable

    table.length should be(10)
  }

  override def afterAll {
  	tmpFile.delete()
  }

  def setupData() {
  	val fos = new FileOutputStream(tmpFile)
  	fos.write(RdbTestIndexDataFixture.generateTestData)
  	fos.close()
  }
}

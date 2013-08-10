package com.joakibj.tswrdb.rdb.export

import java.io.{BufferedInputStream, FileInputStream, File, FileOutputStream}
import org.scalatest.FunSuite
import org.scalatest.ShouldMatchers
import org.scalatest.BeforeAndAfterAll
import org.scalatest.junit.JUnitRunner
import org.junit.runner.RunWith
import com.joakibj.tswrdb.rdb.data.{RdbTestIndexDataFixture, RdbTestDataFixture}
import com.joakibj.tswrdb.rdb.index.RdbIndexEntry
import org.scalatest.PrivateMethodTester._
import com.joakibj.tswrdb.rdb.RdbIOException

@RunWith(classOf[JUnitRunner])
class RdbDataFileReaderTest extends FunSuite with BeforeAndAfterAll with ShouldMatchers {

  val DummyHash = RdbTestIndexDataFixture.DummyHash
  val tmpOutputDirectory: File = createTmpOutputDirectory
  val tmpRdbDataFile1 = new File(tmpOutputDirectory, "01.rdbdata")

  override def beforeAll {
    setupRdbDataFile()
  }

  test("should create RdbDataFileReader object") {
    RdbDataFileReader(tmpRdbDataFile1, indexEntries)
  }

  test("should be able to read data entry header") {
    val reader = RdbDataFileReader(tmpRdbDataFile1, indexEntries)
    val readNextDataEntryHeader = PrivateMethod[RdbDataEntry]('readNextDataEntryHeader)
    val result = reader invokePrivate readNextDataEntryHeader(16)

    result should equal(RdbDataEntry(1000001, 1, 15))
  }

  test("should be able to read data, after reading the data header") {
    val reader = RdbDataFileReader(tmpRdbDataFile1, indexEntries)
    reader.inputStream.skip(16)
    val readData = PrivateMethod[Array[Byte]]('readData)
    val result = reader invokePrivate readData(15)

    new String(result) should equal("IHateMayansSoBd")
  }

  test("should read data entry") {
    val reader = RdbDataFileReader(tmpRdbDataFile1, indexEntries)

    val (dataEntry, buf) = reader.readDataEntry(RdbIndexEntry(1000001, 1, 1, 20, 15, DummyHash), 4)

    dataEntry should equal(RdbDataEntry(1000001, 1, 15, DummyHash))
    buf should equal(new String("IHateMayansSoBd").toArray.map(_ toByte))
  }

  test("should fail if a mismatching data entry was read") {
    val reader = RdbDataFileReader(tmpRdbDataFile1, indexEntries)

    val thrown = intercept[RdbIOException] {
      reader.readDataEntry(RdbIndexEntry(1000000, 1, 1, 20, 15, DummyHash), 4)
    }
    thrown.getMessage should equal("A mismatching data entry was read: " +
      RdbIndexEntry(1000000, 1, 1, 20, 15, DummyHash) + " vs " + RdbDataEntry(1000001, 1, 15))
  }

  test("should read all entries") {
    val reader = RdbDataFileReader(tmpRdbDataFile1, indexEntries)
    val result = reader.readDataEntries()

    result should have size (5)
  }

  def createTmpOutputDirectory: File = {
    val tmp = new File(new File(System.getProperty("java.io.tmpdir")), "tswrdb")
    tmp.mkdir()
    tmp
  }

  override def afterAll {
    tmpOutputDirectory.delete()
  }

  def setupRdbDataFile() {
    val fos = new FileOutputStream(tmpRdbDataFile1)
    fos.write(RdbTestDataFixture.generateTestDataFile1)
    fos.close()
  }

  def indexEntries: Array[RdbIndexEntry] = {
    Array(
      RdbIndexEntry(1000001, 1, 1, 20, 15, DummyHash),
      RdbIndexEntry(1000001, 2, 1, 51, 15, DummyHash),
      RdbIndexEntry(1000001, 3, 1, 82, 15, DummyHash),
      RdbIndexEntry(1000001, 4, 1, 113, 15, DummyHash),
      RdbIndexEntry(1000001, 5, 1, 144, 15, DummyHash)
    )
  }
}
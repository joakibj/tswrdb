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
class RdbDataFileExporterTest extends FunSuite with BeforeAndAfterAll with ShouldMatchers {

  val DummyHash = RdbTestIndexDataFixture.DummyHash
  val tmpOutputDirectory: File = createTmpOutputDirectory
  val tmpRdbDataFile1 = new File(tmpOutputDirectory, "01.rdbdata")

  override def beforeAll {
    setupRdbDataFile()
  }

  test("should create RdbDataFileExporter object") {
    RdbDataFileExporter(tmpOutputDirectory, tmpRdbDataFile1, indexEntries)
  }

  test("should be able to read data entry header") {
    val exporter = RdbDataFileExporter(tmpOutputDirectory, tmpRdbDataFile1, indexEntries)
    val readNextDataEntryHeader = PrivateMethod[RdbDataEntry]('readNextDataEntryHeader)
    val result = exporter invokePrivate readNextDataEntryHeader(0)

    result should equal(RdbDataEntry(1000001, 1, 15))
  }

  test("should be able to read data, after reading the data header") {
    val exporter = RdbDataFileExporter(tmpOutputDirectory, tmpRdbDataFile1, indexEntries)
    exporter.fileInputStream.skip(16)
    val readData = PrivateMethod[Array[Byte]]('readData)
    val result = exporter invokePrivate readData(15)

    new String(result) should equal("IHateMayansSoBd")
  }

  test("should export data entry to file: outputDir/exported/ID") {
    val exporter = RdbDataFileExporter(tmpOutputDirectory, tmpRdbDataFile1, indexEntries)

    val processEntry = PrivateMethod[Unit]('processEntry)
    exporter invokePrivate processEntry(RdbIndexEntry(1000001, 1, 1, 20, 15, DummyHash), 4)

    val result = new File(tmpOutputDirectory, "1.dat")
    result should be a ('file)

    val bufis = new BufferedInputStream(new FileInputStream(result))
    val buf = new Array[Byte](15)
    bufis.read(buf, 0, 15)
    bufis.close()

    buf should equal(new String("IHateMayansSoBd").toArray.map(_ toByte))
  }

  test("should fail if a mismatching data entry was read") {
    val exporter = RdbDataFileExporter(tmpOutputDirectory, tmpRdbDataFile1, indexEntries)

    val processEntry = PrivateMethod[Unit]('processEntry)
    val thrown = intercept[RdbIOException] {
      exporter invokePrivate processEntry(RdbIndexEntry(1000000, 1, 1, 20, 15, DummyHash), 4)
    }

    thrown.getMessage should equal("A mismatching data entry was read: " +
      RdbIndexEntry(1000000, 1, 1, 20, 15, DummyHash) + " vs " + RdbDataEntry(1000001, 1, 15))
  }

  test("should export all entries") {
    val exporter = RdbDataFileExporter(tmpOutputDirectory, tmpRdbDataFile1, indexEntries)
    exporter.exportDataEntries()

    tmpOutputDirectory.listFiles().filter(_.getName.endsWith(".dat")) should have size (5)
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
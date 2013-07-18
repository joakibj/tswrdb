package com.joakibj.tswrdb.rdb

import java.io.File
import java.io.FileOutputStream
import org.scalatest.FunSuite
import org.scalatest.BeforeAndAfterAll
import org.scalatest.junit.JUnitRunner
import org.junit.runner.RunWith

@RunWith(classOf[JUnitRunner])
class RdbIndexFileReaderTest extends FunSuite with BeforeAndAfterAll {

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

  override def afterAll {
  	tmpFile.delete()
  }

  def setupData() {
  	val MagicNumber: Array[Byte] = "IBDR" map(_.toByte) toArray
  	val header: Array[Byte] =  MagicNumber ++ Array[Byte](7, 0, 0, 0) ++ Array.fill(16)(0.toByte) ++ Array[Byte](10, 0, 0, 0)

  	val testData: Array[Byte] = header ++ Array[Byte]()
  	val fos = new FileOutputStream(tmpFile)
  	fos.write(testData)
  	fos.close()
  }

  def generateIndexTable(num: Int): Array[Byte] = {
  	Array[Byte]()
  }
}

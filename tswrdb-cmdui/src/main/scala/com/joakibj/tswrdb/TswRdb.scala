package com.joakibj.tswrdb

import rdb.export.RdbExporter
import java.io.File
import rdb.{RdbType, RdbTypes}

case class Config(rdbDataDirectory: File = new File("."), rdbType: Int = 0)

object TswRdb extends App {
  val parser = new scopt.OptionParser[Config]("tswrdb") {
    head("tswrdb", "0.1")
    opt[File]('r', "rdb") required() valueName ("<directory>") action {
      (file, config) =>
        config.copy(rdbDataDirectory = file)
    } text ("rdb points to the directory that has RDB files and is required.")
    opt[Int]('t', "type") required() action {
      (rtype, config) =>
        config.copy(rdbType = rtype)
    } text ("rdbType of the data that is going to be exported.")
    help("help") text ("prints this usage text.")
    version("version")
    note("Data is exported to the export folder.")
    cmd("list") action {
      (_, config) =>
        listWellUnderstoodRbTypes()
        config
    } text ("Lists the valid rdb types available. Per default and to keep the user sane, only well understood RdbTypes are listed.") children (
      opt[Unit]("all") abbr ("a") action {
        (_, config) =>
          listAllRdbTypes()
          config
      } text ("List all rdbtypes, regardless. Note that some are highly mysterious and/or esoteric. You will have to make sense of them yourself")
      )
  }

  parser.parse(args, Config()) map {
    config =>
      startExport(config)
  } getOrElse {

  }

  def listAllRdbTypes() {
    listRdbTypes(RdbTypes.values)
  }

  def listWellUnderstoodRbTypes() {
    listRdbTypes(RdbTypes.values.filter(_.understood))
  }

  def listRdbTypes(rdbTypes: List[RdbType]) {
    println("RdbType list:")
    rdbTypes foreach {
      (rdbType) =>
        println("\t" + rdbType.id + "\t" + rdbType.name)
    }
    System.exit(0)
  }

  def startExport(config: Config) {
    try {
      RdbExporter(config.rdbDataDirectory).exportAll(config.rdbType)
    } catch {
      case ex: RuntimeException => {
        println(ex.getMessage)
        System.exit(1)
      }
    }
  }
}

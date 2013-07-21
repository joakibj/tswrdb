package com.joakibj.tswrdb

import rdb.export.RdbExporter
import java.io.File
import rdb.{RdbTypeNotFoundException, RdbType, RdbTypes}

object ListRdbTypesMode extends Enumeration {
  val None, All, Understood = Value
}

case class Config(rdbDataDirectory: File = new File("."),
                  rdbType: Int = 0,
                  command: String = "",
                  listMode: Enumeration#Value = ListRdbTypesMode.None)

object TswRdb extends App {
  val parser = new scopt.OptionParser[Config]("tswrdb") {
    head("tswrdb", "0.1")
    help("help") text ("prints this usage text.")
    version("version")
    note("Data is exported to the export folder.")
    cmd("list") action {
      (_, config) =>
        config.copy(command = "list", listMode = ListRdbTypesMode.Understood)
    } text ("Lists the valid rdb types available. Per default and to keep the user sane, only well understood RdbTypes are listed.") children (
      opt[Unit]("all") abbr ("a") action {
        (_, config) =>
          config.copy(listMode = ListRdbTypesMode.All)
      } text ("List all rdbtypes, regardless. Note that some are highly mysterious and/or esoteric. You will have to make sense of them yourself")
      )
    cmd("export") action {
      (_, config) =>
        config.copy(command = "export")
    } text ("Export entries belonging to this rdbtype") children(
      opt[File]('r', "rdb") required() valueName ("<directory>") action {
        (file, config) =>
          config.copy(rdbDataDirectory = file)
      } text ("rdb points to the directory that has RDB files and is required."),
      arg[Int]("<rdbType>") required() action {
        (rdbType, config) =>
          config.copy(rdbType = rdbType)
      } text ("rdbType of the data that is going to be exported.")
      )
  }

  parser.parse(args, Config()) map {
    config =>
      config.command match {
        case "list" =>
          config.listMode match {
            case ListRdbTypesMode.All => listAllRdbTypes()
            case ListRdbTypesMode.Understood => listWellUnderstoodRbTypes()
            case _ => exit()
          }
        case "export" =>
          startExport(config)
        case _ =>
          parser.showUsage
          exit()
      }
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
    exit()
  }

  def startExport(config: Config) {
    try {
      val rdbType = RdbTypes.find(config.rdbType).getOrElse {
        throw new RdbTypeNotFoundException("RdbType: " + config.rdbType + " was not found")
      }
      println("RDB data directory set to: " + config.rdbDataDirectory.getCanonicalPath)
      println("Exporting RdbType: " + rdbType + " into export/" + rdbType.id + " ...")
      RdbExporter(config.rdbDataDirectory).exportAll(config.rdbType)
    } catch {
      case ex: RdbTypeNotFoundException => exit(ex.getMessage, 1)
      case ex: RuntimeException => exit(ex.getMessage, 1)
    }
  }

  def exit(msg: String = "", exitCode: Int = 0) {
    if (msg.length > 0) println(msg)
    println("Exiting...")
    System.exit(exitCode)
  }
}

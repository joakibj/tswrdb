package com.joakibj.tswrdb

import rdb.export.RdbExporter

object TswRdb {
  def main(args: Array[String]) {
    val directory = "D:\\Programs\\TSW TestLive\\RDB"
    RdbExporter(directory).exportAll(1000099)
  }
}

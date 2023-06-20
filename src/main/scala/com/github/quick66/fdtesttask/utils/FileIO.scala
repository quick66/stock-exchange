package com.github.quick66.fdtesttask.utils

import java.nio.charset.StandardCharsets
import java.nio.file.{Files, Path, Paths, StandardOpenOption}
import scala.jdk.StreamConverters._

object FileIO {

  def getPath(res: String): Path = Paths.get(s"src/main/resources/$res")

  def readFile(name: String): Seq[String] = Files.lines(getPath(name)).toScala(List)

  def printFile(name: String, data: Any): Unit = {
    Files.write(
      getPath(name),
      data.toString.getBytes(StandardCharsets.UTF_8),
      StandardOpenOption.CREATE,
      StandardOpenOption.WRITE,
      StandardOpenOption.TRUNCATE_EXISTING
    )
  }

}

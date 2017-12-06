package com.github.sguzman.scala.scal.uber

import java.io.{BufferedWriter, File, FileWriter}
import java.net.SocketTimeoutException

import io.circe.parser.decode
import io.circe.syntax._

import scala.collection.mutable
import scala.io.Source
import scala.util.{Failure, Success}
import scalaj.http.{HttpRequest, HttpResponse}

object Util {
  private val file = new File("./cache.json")
  val map: mutable.Map[String, String] = decode[mutable.Map[String,String]](
    if (file.createNewFile()) "{}"
    else Source.fromFile(file).getLines.mkString("\n")).right.get

  @scala.annotation.tailrec
  def requestUntilSuccess(req: HttpRequest, attempt: Int = 0): HttpResponse[String] =
    util.Try(req.asString) match {
      case Success(v) => v
      case Failure(e) => e match {
        case _: SocketTimeoutException => requestUntilSuccess(req, attempt + 1)
      }
    }

  @scala.annotation.tailrec
  def requestUntilSuccessIndef(req: HttpRequest, predicate: (HttpResponse[String], Int) => Boolean = (_, _) => true, attempt: Int = 0): String =
    if (this.map.contains(req.url)) {
      println(s"Found ${req.url} in cache - retrieving")
      this.map(req.url)
    } else util.Try(req.asString) match {
      case Success(v) => if (
        util.Try(predicate(v, attempt)) match {
          case Success(u) => u
          case Failure(_) => false
        }
      ) {
        println(s"Putting key:${req.url}, value:${v.body}")
        this.map.put(req.url, v.body)
        val bw = new BufferedWriter(new FileWriter(this.file))
        bw.write(this.map.asJson.toString)
        bw.close()
        v.body
      } else requestUntilSuccessIndef(req, predicate, attempt + 1)
      case Failure(e) => e match {
        case _: SocketTimeoutException => requestUntilSuccessIndef(req, predicate, attempt + 1)
      }
    }
}

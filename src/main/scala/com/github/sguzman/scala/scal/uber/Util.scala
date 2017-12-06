package com.github.sguzman.scala.scal.uber

import java.io.{File, FileWriter, PrintWriter}
import java.net.SocketTimeoutException

import fr.hmil.roshttp.HttpRequest
import monix.execution.Scheduler.Implicits.global
import fr.hmil.roshttp.response.SimpleHttpResponse
import io.circe.parser.decode
import io.circe.syntax._

import scala.collection.mutable
import scala.concurrent.Await
import scala.concurrent.duration.Duration
import scala.io.Source
import scala.util.{Failure, Success}

object Util {
  private val file = new File("./cache.json")
  if (file.createNewFile()) {
    val bw = new PrintWriter(new FileWriter(this.file))
    bw.write("{}")
    bw.close()
  }
  val map: mutable.Map[String, String] = decode[mutable.Map[String,String]](
    Source.fromFile(file).getLines.mkString("\n")).right.get

  @scala.annotation.tailrec
  def requestUntilSuccess(req: HttpRequest, attempt: Int = 0): SimpleHttpResponse =
    util.Try(Await.result(req.send, Duration.Inf)) match {
      case Success(v) => v
      case Failure(e) => e match {
        case _: SocketTimeoutException => requestUntilSuccess(req, attempt + 1)
      }
    }

  @scala.annotation.tailrec
  def requestUntilSuccessIndef(req: HttpRequest, predicate: (SimpleHttpResponse, Int) => Boolean = (_, _) => true, attempt: Int = 0): String =
    if (this.map.contains(req.url)) {
      println(s"Found ${req.url} in cache - retrieving")
      this.map(req.url)
    } else util.Try(Await.result(req.send, Duration.Inf)) match {
      case Success(v) => if (
        util.Try(predicate(v, attempt)) match {
          case Success(u) => u
          case Failure(_) => false
        }
      ) {
        this.map.put(req.url, v.body)
        val bw = new PrintWriter(new FileWriter(this.file))
        bw.write(this.map.asJson.toString)
        bw.close()
        v.body
      } else requestUntilSuccessIndef(req, predicate, attempt + 1)
      case Failure(e) => e match {
        case _: SocketTimeoutException => requestUntilSuccessIndef(req, predicate, attempt + 1)
      }
    }

  def getRequest(url: String, cookie: String): HttpRequest =
    HttpRequest(url).withHeader("Cookie", cookie)

  def getRequest(url: String): HttpRequest = HttpRequest(url)
}

package com.github.sguzman.scala.scal.uber

import java.net.SocketTimeoutException

import io.circe.parser.parse

import scala.util.{Failure, Success}
import scalaj.http.Http

object Main {
  def main(args: Array[String]): Unit = {
    val initCookie = args.head
    val url = "https://partners.uber.com/p3/platform_chrome_nav_data"
    val logged = loggedIn(initCookie)
    println(logged)
  }

  def body(url: String, cookie: String): String =
    util.Try(Http(url).header("Cookie", cookie).asString) match {
      case Success(v) => v.body
      case Failure(e) => e match {
        case _: SocketTimeoutException => body(url, cookie)
      }
    }

  def loggedIn(cookie: String): Boolean =
    parse(body("https://partners.uber.com/p3/platform_chrome_nav_data", cookie)
    ) match {
      case Left(_) => false
      case Right(_) => true
    }
}

package com.github.sguzman.scala.scal.uber

import io.circe.parser._

import scalaj.http.{Http, HttpResponse}

object Main {
  def main(args: Array[String]): Unit = {
    val initCookie = args.head
    val url = "https://partners.uber.com/p3/platform_chrome_nav_data"
    val logged = get(url, initCookie)
    val json = parse(logged.body)
    println(json)
  }

  def get(url: String, cookie: String): HttpResponse[String] =
    Http(url).header("Cookie", cookie).asString
}

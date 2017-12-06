package com.github.sguzman.scala.scal.uber

import com.github.sguzman.scala.scal.uber.json.typesafe.nav.Item
import com.google.gson.GsonBuilder

import scala.util.{Failure, Success}
import scalaj.http.{Http, HttpResponse}

object Main {
  def main(args: Array[String]): Unit = {
    val initCookie = args.head
    val logged = loggedIn(initCookie)

    val cookie = if (logged) initCookie
    else logMeIn(cookie)


    println(logged)
  }

  def get(url: String, cookie: String): HttpResponse[String] =
    util.Try(Http(url).header("Cookie", cookie).asString) match {
      case Success(v) => v
      case Failure(e) => get(url, cookie)
    }

  def loggedIn(cookie: String): Boolean =
    util.Try(
      new GsonBuilder().create.fromJson(
        get("https://partners.uber.com/p3/platform_chrome_nav_data", cookie).body
      , classOf[Item])) match {
      case Success(_) => true
      case Failure(_) => false
    }

  def logMeIn(firstCookie: String): String = {
    ""
  }
}

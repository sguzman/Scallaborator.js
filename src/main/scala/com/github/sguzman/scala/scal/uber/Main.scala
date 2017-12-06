package com.github.sguzman.scala.scal.uber

import com.github.sguzman.scala.scal.uber.json.typesafe.login.email.input.{Answer, Email, UserIdentifier}
import com.github.sguzman.scala.scal.uber.json.typesafe.nav.Item
import com.google.gson.GsonBuilder

import scala.util.{Failure, Success}
import scalaj.http.{Http, HttpResponse}

object Main {
  def main(args: Array[String]): Unit = {
    val initCookie = args.head
    val logged = loggedIn(initCookie)
    val email = args(1)
    val pass = args(2)

    val cookie = if (logged) initCookie
    else logMeIn(initCookie, email, pass)

    println(cookie)
  }

  def get(url: String, cookie: String): HttpResponse[String] =
    Http(url).header("Cookie", cookie).asString

  def loggedIn(cookie: String): Boolean =
    util.Try(
      new GsonBuilder().create.fromJson(
        get("https://partners.uber.com/p3/platform_chrome_nav_data", cookie).body
      , classOf[Item])) match {
      case Success(_) => true
      case Failure(_) => false
    }

  def logMeIn(firstCookie: String, email: String, pass: String): String = {
    val bodyObj =
      Email(Answer("VERIFY_INPUT_EMAIL", UserIdentifier(email)), init = true)

    val gson = new GsonBuilder().create
    val body = gson.toJson(bodyObj)
    println(body)

    val url = "https://auth.uber.com/login/handleanswer"
    val response = Http(url).postData(body).asString
    response.body
  }
}

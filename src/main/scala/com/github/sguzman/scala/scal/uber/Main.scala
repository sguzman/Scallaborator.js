package com.github.sguzman.scala.scal.uber

import java.net.SocketTimeoutException

import com.github.sguzman.scala.scal.uber.json.typesafe.login.email.input.{Answer, Email, UserIdentifier}
import com.github.sguzman.scala.scal.uber.json.typesafe.login.email.output.EmailOutput
import io.circe.generic.auto._
import io.circe.parser.parse
import io.circe.parser.decode
import io.circe.syntax._

import scala.util.{Failure, Success}
import scalaj.http.{Http, HttpRequest, HttpResponse}

object Main {
  def main(args: Array[String]): Unit = {
    val initCookie = args.head
    val email = args(1)
    val pass = args(2)

    val logged = logMeIn(initCookie, email, pass)
    println(logged.body)

    val obj = decode[EmailOutput](logged.body)
    println(obj)
  }

  def requestUntilSuccess(req: HttpRequest): HttpResponse[String] =
    util.Try(req.asString) match {
      case Success(v) => v
      case Failure(e) => e match {
        case _: SocketTimeoutException => requestUntilSuccess(req)
      }
    }

  def loggedIn(cookie: String): Boolean =
    parse(
      requestUntilSuccess(
        Http("https://partners.uber.com/p3/platform_chrome_nav_data")
          .header("Cookie", cookie)).body
    ) match {
      case Left(_) => false
      case Right(_) => true
    }

  def logMeIn(initCookie: String, email: String, pass: String) = {
    val url = "https://auth.uber.com/login/?next_url=https%3A%2F%2Fpartners.uber.com"
    val getRequest = Http(url).header("Cookie", initCookie)

    val response = requestUntilSuccess(getRequest)
    val getCookie = response.cookies.mkString("; ")
    val crsfToken = response.header("x-csrf-token").get

    val objectEmailStr = Email(Answer("VERIFY_INPUT_EMAIL", UserIdentifier(email)), init = true)
    val emailPayload = objectEmailStr.asJson.toString

    val postUrl = "https://auth.uber.com/login/handleanswer"
    val emailRequest = Http(postUrl)
      .header("Cookie", getCookie)
      .header("x-csrf-token", crsfToken)
      .header("Content-Type", "application/json")
      .postData(emailPayload)
    val emailResponse = requestUntilSuccess(emailRequest)
    emailResponse
  }
}

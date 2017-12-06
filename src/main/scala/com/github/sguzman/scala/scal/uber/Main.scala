package com.github.sguzman.scala.scal.uber

import java.net.SocketTimeoutException

import com.github.sguzman.scala.scal.uber.json.typesafe.login.email.input.{Answer, Email, UserIdentifier}
import com.github.sguzman.scala.scal.uber.json.typesafe.login.email.output.EmailOutput
import com.github.sguzman.scala.scal.uber.json.typesafe.login.password.input
import com.github.sguzman.scala.scal.uber.json.typesafe.login.password.input.Password
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
    println(logged)
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
    val response = getLoginPage(initCookie)

    val emailResponse = postEmail(response, email)
    val _ = decode[EmailOutput](emailResponse.body)

    val password = postPassword(emailResponse, pass)
    val _ = decode[Password](password.body)
    //TODO - implement login features
    initCookie
  }

  def getLoginPage(cookie: String): HttpResponse[String] = {
    val url = "https://auth.uber.com/login/?next_url=https%3A%2F%2Fpartners.uber.com"
    val request = Http(url).header("Cookie", cookie)

    val response = requestUntilSuccess(request)
    response
  }

  def postEmail(response: HttpResponse[String], email: String): HttpResponse[String] = {
    val cookie = response.cookies.mkString("; ")
    val crsfToken = response.header("x-csrf-token").get

    val emailObj = Email(Answer("VERIFY_INPUT_EMAIL", UserIdentifier(email)), init = true)
    val emailPayload = emailObj.asJson.toString

    val postUrl = "https://auth.uber.com/login/handleanswer"
    val emailRequest = Http(postUrl)
      .header("Cookie", cookie)
      .header("x-csrf-token", crsfToken)
      .header("Content-Type", "application/json")
      .postData(emailPayload)

    val emailResponse = requestUntilSuccess(emailRequest)
    emailResponse
  }

  def postPassword(response: HttpResponse[String], pass: String): HttpResponse[String] = {
    val cookie = response.cookies.mkString("; ")
    val crsfToken = response.header("x-csrf-token").get

    val passObj = Password(input.Answer("VERIFY_PASSWORD", pass), rememberMe = true)
    val body = passObj.asJson.toString

    val postUrl = "https://auth.uber.com/login/handleanswer"
    val passRequest = Http(postUrl)
      .header("Cookie", cookie)
      .header("x-csrf-token", crsfToken)
      .header("Content-Type", "application/json")
      .postData(body)

    val passResponse = requestUntilSuccess(passRequest)
    passResponse
  }
}

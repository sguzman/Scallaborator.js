package com.github.sguzman.scala.scal.uber

import com.github.sguzman.scala.scal.uber.Util.requestUntilSuccess
import com.github.sguzman.scala.scal.uber.json.typesafe.login.email.input.{Answer, Email, UserIdentifier}
import com.github.sguzman.scala.scal.uber.json.typesafe.login.email.output.EmailOutput
import com.github.sguzman.scala.scal.uber.json.typesafe.login.password.input
import com.github.sguzman.scala.scal.uber.json.typesafe.login.password.input.Password
import io.circe.generic.auto._
import io.circe.parser.{decode, parse}
import io.circe.syntax._

import scalaj.http.HttpResponse

object Login {
  def loggedIn(cookie: String): Boolean =
    parse(
      requestUntilSuccess(
        Util.getRequest("https://partners.uber.com/p3/platform_chrome_nav_data", cookie)
      ).body
    ) match {
      case Left(_) => false
      case Right(_) => true
    }

  def logMeIn(initCookie: String, email: String, pass: String) = {
    val response = getLoginPage(initCookie)

    val emailResponse = postEmail(response, email)
    decode[EmailOutput](emailResponse.body)

    val password = postPassword(emailResponse, pass)
    decode[Password](password.body)
    //TODO - implement login features
    initCookie
  }

  def getLoginPage(cookie: String): HttpResponse[String] = {
    val url = "https://auth.uber.com/login/?next_url=https%3A%2F%2Fpartners.uber.com"
    val request = Util.getRequest(url, cookie)

    val response = requestUntilSuccess(request)
    response
  }

  def postEmail(response: HttpResponse[String], email: String): HttpResponse[String] = {
    val cookie = response.cookies.mkString("; ")
    val crsfToken = response.header("x-csrf-token").get

    val emailObj = Email(Answer("VERIFY_INPUT_EMAIL", UserIdentifier(email)), init = true)
    val emailPayload = emailObj.asJson.toString

    val postUrl = "https://auth.uber.com/login/handleanswer"
    val emailRequest = Util.postDataCSRF(postUrl, cookie, emailPayload, crsfToken)

    val emailResponse = requestUntilSuccess(emailRequest)
    emailResponse
  }

  def postPassword(response: HttpResponse[String], pass: String): HttpResponse[String] = {
    val cookie = response.cookies.mkString("; ")
    val crsfToken = response.header("x-csrf-token").get

    val passObj = Password(input.Answer("VERIFY_PASSWORD", pass), rememberMe = true)
    val body = passObj.asJson.toString

    val postUrl = "https://auth.uber.com/login/handleanswer"
    val passRequest = Util.postDataCSRF(postUrl, cookie, body, crsfToken)

    val passResponse = requestUntilSuccess(passRequest)
    passResponse
  }
}

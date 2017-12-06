package com.github.sguzman.scala.scal.uber

import java.net.SocketTimeoutException

import scala.util.{Failure, Success}
import scalaj.http.{HttpRequest, HttpResponse}

object Util {
  @scala.annotation.tailrec
  def requestUntilSuccess(req: HttpRequest): HttpResponse[String] =
    util.Try(req.asString) match {
      case Success(v) => v
      case Failure(e) => e match {
        case _: SocketTimeoutException => requestUntilSuccess(req)
      }
    }
}

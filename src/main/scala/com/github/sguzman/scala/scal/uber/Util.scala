package com.github.sguzman.scala.scal.uber

import java.net.SocketTimeoutException

import scala.util.{Failure, Success}
import scalaj.http.{HttpRequest, HttpResponse}

object Util {
  @scala.annotation.tailrec
  def requestUntilSuccess(req: HttpRequest, predicate: (HttpResponse[String], Int) => Boolean, attempt: Int = 0): HttpResponse[String] =
    util.Try(req.asString) match {
      case Success(v) => if (
        util.Try(predicate(v, attempt)) match {
          case Success(u) => u
          case Failure(_) => false
        }
      ) v else requestUntilSuccess(req, predicate, attempt + 1)
      case Failure(e) => e match {
        case _: SocketTimeoutException => requestUntilSuccess(req, predicate, attempt + 1)
      }
    }
}

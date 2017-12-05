package com.github.sguzman.scala.scal.uber

import fr.hmil.roshttp.HttpRequest
import fr.hmil.roshttp.Protocol.HTTPS
import monix.execution.Scheduler.Implicits.global

import scala.concurrent.Await
import scala.concurrent.duration.Duration

object Main {
  def main(args: Array[String]): Unit = {
    val cookie = args.head

    val request = HttpRequest()
      .withProtocol(HTTPS)
      .withHost("partners.uber.com")
      .withPath("/p3/platform_chrome_nav_data")

    val responseFuture = request.send
    val response = Await.result(responseFuture, Duration.Inf)
    println(response.statusCode)
    println(response.body)
  }
}

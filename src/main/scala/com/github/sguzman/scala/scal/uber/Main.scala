package com.github.sguzman.scala.scal.uber

import com.github.sguzman.scala.scal.uber.json.typesafe.nav.Item
import com.google.gson.GsonBuilder

import scalaj.http.Http

object Main {
  def main(args: Array[String]): Unit = {
    val cookie = args.head

    val request = Http("https://partners.uber.com/p3/platform_chrome_nav_data")
    val response = request.header("Cookie", cookie).asString

    val gson = new GsonBuilder().create

    val json = gson.fromJson(response.body, classOf[Item])
    println(json)
  }
}

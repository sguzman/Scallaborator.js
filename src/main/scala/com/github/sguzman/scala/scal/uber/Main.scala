package com.github.sguzman.scala.scal.uber

import com.github.sguzman.scala.scal.uber.json.typesafe.data.all_data.AllDataStatement
import com.github.sguzman.scala.scal.uber.json.typesafe.data.statement.Statement
import io.circe.generic.auto._
import io.circe.parser.decode

import scalaj.http.Http

object Main {
  def main(args: Array[String]): Unit = {
    val cookie = args.head
    val allData = Util.requestUntilSuccess(
      Http("https://partners.uber.com/p3/money/statements/all_data/")
        .header("Cookie", cookie)
    )

    val str = allData.body
    val allDataStatements = decode[Array[AllDataStatement]](str).right.get
    val statementUUIDs = allDataStatements map (_.uuid)
    val statementResponses = statementUUIDs
      .par
      .map(u => s"https://partners.uber.com/p3/money/statements/view/$u")
      .map(u => Util.requestUntilSuccess(
        Http(u).header("Cookie", cookie)
      ))
      .map(_.body)
      .filter(_.nonEmpty)
      .map(decode[Statement])
      .map(_.right)
      .map(_.get)
      .map(_.body)
      .map(_.driver)
      .map(_.trip_earnings)
      .map(_.trips)
      .map(_.keySet)
      .flatMap(_.toList)

    println(statementResponses.length)
  }
}

package com.github.sguzman.scala.scal.uber

import java.util.UUID

import com.github.sguzman.scala.scal.uber.args.{Config, Parser}
import com.github.sguzman.scala.scal.uber.json.typesafe.data.all_data.AllDataStatement
import com.github.sguzman.scala.scal.uber.json.typesafe.data.statement.Statement
import com.github.sguzman.scala.scal.uber.json.typesafe.data.trip.Trip
import io.circe.generic.auto._
import io.circe.parser.decode

import scala.util.{Failure, Success}

object Main {
  def main(args: Array[String]): Unit = {
    val config = new Parser().parse(args, Config()) match {
      case Some(c) => c
      case None =>
        System.exit(1)
        Config()
    }

    val allData = Util.requestUntilSuccess(
      Util.getRequest("https://partners.uber.com/p3/money/statements/all_data/", config.cookie)
    )

    val str = allData.body
    val allDataStatements = decode[Array[AllDataStatement]](str).right.get
    val statementUUIDs = allDataStatements map (_.uuid)

    val partialToUUID = toTripUUID(config.cookie, _: UUID)
    val tripUUIDS = statementUUIDs.flatMap(partialToUUID)
    println(tripUUIDS.length)

    val partialToTrip = trip(config.cookie, _: UUID)
    val grabTrip = tripUUIDS.map(partialToTrip)
    println(grabTrip)
  }

  def toTripUUID(cookie: String, statementUUID: UUID): List[UUID] = {
    val url = s"https://partners.uber.com/p3/money/statements/view/$statementUUID"
    val request = Util.getRequest(url, cookie)
    val response = Util.requestUntilSuccessIndef(request, (t, i) => {
      util.Try(decode[Statement](t.body).right.get.body.driver.trip_earnings.trips.keySet.toList) match {
        case Success(_) =>
          println(s"!!!$statementUUID!!! @ $i")
          true
        case Failure(_) => false
      }
    })

    val obj = decode[Statement](response)
    val right = obj.right
    val getRight = right.get
    val uuids = getRight.body.driver.trip_earnings.trips.keySet.toList
    uuids
  }

  def trip(cookie: String, tripUUID: UUID): Trip = {
    val url = s"https://partners.uber.com/p3/money/trips/trip_data/$tripUUID"
    val request = Util.getRequest(url, cookie)
    val response = Util.requestUntilSuccessIndef(request, (t, i) => {
      util.Try(decode[Trip](t.body).right.get) match {
        case Success(_) =>
          println(s"!!!$tripUUID!!! @ $i")
          true
        case Failure(_) => false
      }
    })

    val body = response
    val obj = decode[Trip](body).right.get
    obj
  }
}

package com.github.sguzman.scala.scal.uber

import java.util.UUID

import com.github.sguzman.scala.scal.uber.json.typesafe.data.all_data.AllDataStatement
import com.github.sguzman.scala.scal.uber.json.typesafe.data.statement.Statement
import com.github.sguzman.scala.scal.uber.json.typesafe.data.trip.Trip
import io.circe.generic.auto._
import io.circe.parser.decode

import scala.util.{Failure, Success}
import scalaj.http.Http

object Main {
  def main(args: Array[String]): Unit = {
    val cookie = args.head
    val allData = Util.requestUntilSuccess(
      Http("https://partners.uber.com/p3/money/statements/all_data/")
        .header("Cookie", cookie))

    val str = allData.body
    val allDataStatements = decode[Array[AllDataStatement]](str).right.get
    val statementUUIDs = allDataStatements map (_.uuid)

    val partialToUUID = toTripUUID(cookie, _: UUID)
    val tripUUIDS = statementUUIDs.par.flatMap(partialToUUID)
    println(tripUUIDS.length)

    val partialToTrip = trip(cookie, _: UUID)
    val grabTrip = tripUUIDS.par.map(partialToTrip)
    println(grabTrip)
  }

  def toTripUUID(cookie: String, statementUUID: UUID): List[UUID] = {
    val url = s"https://partners.uber.com/p3/money/statements/view/$statementUUID"
    val request = Http(url).header("Cookie", cookie)
    val response = Util.requestUntilSuccess(request, (t, i) => {
      val obj = decode[Statement](t.body)
      if (obj.isLeft) println(obj)
      util.Try(decode[Statement](t.body).right.get.body.driver.trip_earnings.trips.keySet.toList) match {
        case Success(v) =>
          println(s"!!!$statementUUID!!! @ $i")
          true
        case Failure(e) => false
      }
    })

    val obj = decode[Statement](response.body)
    val right = obj.right
    val getRight = right.get
    val uuids = getRight.body.driver.trip_earnings.trips.keySet.toList
    uuids
  }

  def trip(cookie: String, tripUUID: UUID): Trip = {
    val url = s"https://partners.uber.com/p3/money/trips/trip_data/$tripUUID"
    val request = Http(url).header("Cookie", cookie)
    val response = Util.requestUntilSuccess(request, (t, i) => {
      val obj = decode[Trip](t.body)
      if (obj.isLeft) println(obj)
      util.Try(decode[Trip](t.body).right.get) match {
        case Success(v) =>
          println(s"!!!$tripUUID!!! @ $i")
          true
        case Failure(e) => false
      }
    })

    val body = response.body
    val obj = decode[Trip](body).right.get
    obj
  }
}

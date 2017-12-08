package com.github.sguzman.scala.scal.uber

import com.github.sguzman.scala.scal.uber.safetyfirst.{Tweet, TweetTopLevel}
import fr.hmil.roshttp.HttpRequest
import google.maps.LatLng
import io.circe.generic.auto._
import io.circe.parser.decode
import monix.execution.Scheduler.Implicits.global
import org.scalajs.dom.document

import scala.scalajs.js
import scala.util.{Failure, Success}

object Main {
  var gmap: Option[google.maps.Map] = None
  def main(args: Array[String]): Unit = {
    google.maps.event.addDomListener(org.scalajs.dom.window, "load", initialize())

    val url = "https://tranquil-island-19340.herokuapp.com/"
    val request = HttpRequest(url)
    request.send
      .map(_.body)
      .map(decode[TweetTopLevel])
      .map(_.right)
      .map(_.get)
      .map(_.tweets)
      .onComplete({
        case tweets: Success[Array[Tweet]] =>
          val latLngs = tweets
            .value
            .flatMap(_.text.split("\\*"))
            .filter("[0-9] -[0-9]".r.findFirstIn(_).isDefined)
            .map(_.split(" "))
            .map(t => {
              val (preA, postA) = t.head.splitAt(2)
              val (preB, postB) = t(1).splitAt(4)
              (preA ++ "." ++ postA, preB ++ "." ++ postB)
            })
            .map(t => (t._1.toDouble, t._2.toDouble))
            .map(t => new google.maps.LatLng(t._1, t._2))
          latLngs.toList foreach println
        case error: Failure[Array[Tweet]] => Console.err.println(error)
    })
  }

  def initialize() = js.Function {
    val opts = google.maps.MapOptions(
      center = new LatLng(34.414513, -119.861720),
      zoom = 13,
      panControl = false,
      streetViewControl = false,
      mapTypeControl = false
    )
    this.gmap = Some(new google.maps.Map(document.getElementById("map"), opts))
    ""
  }
}

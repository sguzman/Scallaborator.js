package com.github.sguzman.scala.scal.uber

import com.github.sguzman.scala.scal.uber.safetyfirst.TweetTopLevel
import fr.hmil.roshttp.HttpRequest
import fr.hmil.roshttp.response.SimpleHttpResponse
import google.maps.LatLng
import io.circe.generic.auto._
import io.circe.parser.decode
import monix.execution.Scheduler.Implicits.global
import org.scalajs.dom.document

import scala.scalajs.js
import scala.util.{Failure, Success}
import scalatags.JsDom.all._

object Main {
  var gmap: Option[google.maps.Map] = None
  def main(args: Array[String]): Unit = {
    google.maps.event.addDomListener(org.scalajs.dom.window, "load", initialize())

    val url = "https://tranquil-island-19340.herokuapp.com/"
    val request = HttpRequest(url)
    request.send.onComplete({
      case res: Success[SimpleHttpResponse] =>
        val body = res.get.body
        val tweets = decode[TweetTopLevel](body).right.get.tweets
        val tweetAnchor = document.getElementById("tweets")
        tweets foreach (t => {
          tweetAnchor.appendChild(
            div(
              h1(t.id),
              h3(t.created_at),
              p(t.text)
            ).render)
        })
      case error: Failure[SimpleHttpResponse] => Console.err.println(error)
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

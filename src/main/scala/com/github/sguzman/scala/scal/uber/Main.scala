package com.github.sguzman.scala.scal.uber

import google.maps.LatLng
import org.scalajs.dom.document

import scala.scalajs.js

object Main {
  def main(args: Array[String]): Unit = {
    google.maps.event.addDomListener(org.scalajs.dom.window, "load", initialize())
  }

  def initialize() = js.Function {
    val opts = google.maps.MapOptions(
      center = new LatLng(34.414513, -119.861720),
      zoom = 13,
      panControl = false,
      streetViewControl = false,
      mapTypeControl = false
    )
    val gmap = new google.maps.Map(document.getElementById("map"), opts)
    ""
  }
}

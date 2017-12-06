package com.github.sguzman.scala.scal.uber.json.typesafe.nav

case class Payments(
                   urls: Array[String],
                   primarySideNav: Int,
                   name: String,
                   allowChildMatching: Boolean,
                   nav: NavPayment
                   )

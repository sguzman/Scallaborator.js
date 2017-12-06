package com.github.sguzman.scala.scal.uber.json.typesafe.nav

case class Payments(
                   urls: Urls,
                   primarySideNav: Int,
                   name: String,
                   allowChildMatching: Boolean,
                   nav: NavPayment
                   )

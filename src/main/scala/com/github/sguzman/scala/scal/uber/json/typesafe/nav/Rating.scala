package com.github.sguzman.scala.scal.uber.json.typesafe.nav

case class Rating(
                 subject: Subject,
                 aggregateType: String,
                 displayValue: String,
                 histogram: Map[String, Int]
                 )

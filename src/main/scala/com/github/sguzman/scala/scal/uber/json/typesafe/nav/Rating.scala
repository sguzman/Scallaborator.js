package com.github.sguzman.scala.scal.uber.json.typesafe.nav

import scala.collection.immutable.HashMap

case class Rating(
                 subject: Subject,
                 aggregateType: String,
                 displayValue: String,
                 histogram: HashMap[String, Int]
                 )

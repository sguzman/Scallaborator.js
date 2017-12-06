package com.github.sguzman.scala.scal.uber.json.typesafe.data.trip

case class Item(
               description: String,
               icon: Option[String],
               amount: String,
               itemType: String,
               shouldShowPlusSign: Boolean,
               disclaimer: String,
               recognizedAt: Option[Long]
               )

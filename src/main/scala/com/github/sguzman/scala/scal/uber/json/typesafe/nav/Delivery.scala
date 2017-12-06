package com.github.sguzman.scala.scal.uber.json.typesafe.nav

import java.nio.file.Path

case class Delivery(
                     url: Path,
                     name: String,
                     translationKey: String,
                     primarySideNav: Int
                   )

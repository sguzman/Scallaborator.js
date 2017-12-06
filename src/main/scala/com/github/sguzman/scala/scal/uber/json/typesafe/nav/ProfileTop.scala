package com.github.sguzman.scala.scal.uber.json.typesafe.nav

import java.nio.file.Path

case class ProfileTop(
                       url: Path,
                       primarySideNav: Int,
                       name: String,
                       nav: NavProfile
                     )

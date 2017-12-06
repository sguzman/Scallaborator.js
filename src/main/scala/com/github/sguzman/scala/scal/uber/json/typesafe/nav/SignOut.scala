package com.github.sguzman.scala.scal.uber.json.typesafe.nav

import java.nio.file.Path

case class SignOut(
                    name: String,
                    url: Path,
                    secondarySideNav: Int
                  )

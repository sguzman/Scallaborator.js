package com.github.sguzman.scala.scal.uber.json.typesafe.nav

import java.nio.file.Path

case class Referrals(
                    primarySideNav: Int,
                    nav: NavInvites,
                    name: String,
                    urls: Array[Path]
                    )

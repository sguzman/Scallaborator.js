package com.github.sguzman.scala.scal.uber.json.typesafe.nav

case class Item(
               nav: TopNav,
               user: User,
               rating: Rating,
               translations: Map[String, String],
               avatars: Avatars,
               languages: Array[Language]
               )

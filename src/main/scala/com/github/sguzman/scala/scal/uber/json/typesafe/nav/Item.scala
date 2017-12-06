package com.github.sguzman.scala.scal.uber.json.typesafe.nav

import scala.collection.immutable.HashMap

case class Item(
               nav: TopNav,
               user: User,
               rating: Rating,
               translations: HashMap[String, String],
               avatars: Avatars,
               languages: Array[Language]
               )

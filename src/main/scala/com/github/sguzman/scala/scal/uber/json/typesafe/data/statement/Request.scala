package com.github.sguzman.scala.scal.uber.json.typesafe.data.statement

case class Request(
                  uri: URI,
                  method: String,
                  headers: Map[String,String]
                  )

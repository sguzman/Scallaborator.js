package com.github.sguzman.scala.scal.uber.json.typesafe.data.trip

case class Breakdown(
                    category: String,
                    items: Array[Item],
                    total: String,
                    description: String
                    )

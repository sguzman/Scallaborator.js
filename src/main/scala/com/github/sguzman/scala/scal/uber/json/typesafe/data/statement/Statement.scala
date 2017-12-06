package com.github.sguzman.scala.scal.uber.json.typesafe.data.statement

case class Statement(
                    statusCode: Int,
                    body: Body,
                    headers: Map[String,String],
                    request: Request,

                    )

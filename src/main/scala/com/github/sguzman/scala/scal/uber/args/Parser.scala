package com.github.sguzman.scala.scal.uber.args

class Parser extends scopt.OptionParser[Config]("ScalUber") {
  override def showUsageOnError = true
  head("ScalUber", "v1.0")

  opt[String]('c', "cookie")
  .required
  .abbr("-c")
  .valueName("<cookie>")
  .minOccurs(n = 1)
  .maxOccurs(n = 1)
  .action((x, c) => c.copy(cookie = x))
  .text("Uber Session Cookie")

  help("help").text("prints this usage text")
}

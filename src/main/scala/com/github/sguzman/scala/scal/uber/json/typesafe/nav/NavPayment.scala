package com.github.sguzman.scala.scal.uber.json.typesafe.nav

case class NavPayment(
                     paymentStatements: PaymentStatements,
                     instantPay: InstantPay,
                     taxes: Taxes,
                     banking: Banking
                     )

package com.github.sguzman.scala.scal.uber.json.typesafe.data.statement

import java.util.UUID

case class Trip(
               fare: String,
               status: String,
               marketplace: String,
               join_and_support_eligible: Boolean,
               is_star_power: Boolean,
               uber_fee: String,
               total_earned: String,
               `type`: String,
               cash_collected: String,
               dropoff_at: String,
               request_at: String,
               date: String,
               begintrip_at: String,
               trip_chaining: TripChaining,
               duration: String,
               is_cash_trip: Boolean,
               total: String,
               trip_id: UUID,
               currency_code: String,
               distance: String
               )

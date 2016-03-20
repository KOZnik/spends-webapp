package pl.koznik.spends.gatling

import com.typesafe.config.{ConfigFactory, Config}

import io.gatling.core.Predef._
import io.gatling.http.Predef._

class StoreSpendSimulation extends Simulation {

  val config:Config = ConfigFactory.load()

  val httpProtocol = http
    .baseURL(config.getString("performance.host"))

  val scn = scenario("Store spend")
    .exec(http("Storing spend")
    .post("/spends/resources/spends")
    .formParam("category", "DIFFERENT")
    .formParam("amount", 10)
    .formParam("description", "gatling2")
    .formParam("date", "2016-02-01T10:11:30")
    .check(status.is(200)))

  setUp(scn.inject(atOnceUsers(1))).protocols(httpProtocol)
}
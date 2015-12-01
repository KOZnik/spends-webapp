package pl.koznik.spends.gatling

import com.typesafe.config.{ConfigFactory, Config}

import io.gatling.core.Predef._
import io.gatling.http.Predef._

class StoreSpendSimulation extends Simulation {

  val config:Config = ConfigFactory.load()

  val httpProtocol = http
    .baseURL(config.getString("performance.host"))

  val scn = scenario("Store spend")
    .exec(http("Storing random spend")
    .post("/spends-webapp/resources/spends")
    .check(status.is(200)))

  setUp(scn.inject(atOnceUsers(20))).protocols(httpProtocol)
}
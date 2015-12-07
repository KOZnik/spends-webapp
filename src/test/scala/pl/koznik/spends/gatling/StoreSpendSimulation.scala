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
    .post("/spends-app/resources/spends")
    .formParam("category", "DIFFERENT")
    .formParam("amount", 10)
    .formParam("description", "gatling")
    .check(status.is(200)))

  setUp(scn.inject(atOnceUsers(1000))).protocols(httpProtocol)
}
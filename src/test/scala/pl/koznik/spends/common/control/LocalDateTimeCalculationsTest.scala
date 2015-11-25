package pl.koznik.spends.common.control

import java.time.LocalDateTime

import org.scalatest.Matchers

class LocalDateTimeCalculationsTest extends org.scalatest.FlatSpec with Matchers {
  "Calculator" should "calculate proper begin of month date" in {
    LocalDateTimeCalculations.beginOfMonth(2015, 9) should equal(LocalDateTime.of(2015, 9, 1, 0, 0, 0))
    LocalDateTimeCalculations.beginOfMonth(2015, 2) should equal(LocalDateTime.of(2015, 2, 1, 0, 0, 0))
  }

  it should "calculate proper end of month date" in {
    LocalDateTimeCalculations.endOfMonth(2015, 9) should equal(LocalDateTime.of(2015, 9, 30, 23, 59, 59))
    LocalDateTimeCalculations.endOfMonth(2015, 2) should equal(LocalDateTime.of(2015, 2, 28, 23, 59, 59))
  }
}

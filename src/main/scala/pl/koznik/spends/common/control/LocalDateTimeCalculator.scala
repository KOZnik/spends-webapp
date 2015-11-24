package pl.koznik.spends.common.control

import java.time.temporal.TemporalAdjusters._
import java.time.{LocalDate, LocalDateTime}

object LocalDateTimeCalculator {

  def beginOfMonth(year: Int, month: Int): LocalDateTime =
    LocalDate.now().withYear(year).withMonth(month).`with`(firstDayOfMonth()).atStartOfDay

  def endOfMonth(year: Int, month: Int): LocalDateTime =
    LocalDate.now().withYear(year).withMonth(month).`with`(lastDayOfMonth()).atTime(23, 59, 59)

}

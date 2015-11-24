package pl.koznik.spends.statistics.control

import java.time.LocalDate

import pl.koznik.spends.core.entity.Category._

trait StatisticsCalculator {

  def recalculateForSpendData(category: Category, amount: Double, date: LocalDate)

}

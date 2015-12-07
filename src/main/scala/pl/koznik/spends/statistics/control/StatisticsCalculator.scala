package pl.koznik.spends.statistics.control

import pl.koznik.spends.core.entity.Spend

trait StatisticsCalculator {

  def recalculateForSpendData(spends: List[Spend])

}

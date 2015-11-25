package pl.koznik.spends.statistics.control

import java.util.function.Consumer
import javax.enterprise.event.{Observes, TransactionPhase}
import javax.enterprise.inject.Instance
import javax.inject.Inject

import pl.koznik.spends.core.entity.Spend

class StatisticsCalculatorObserver {

  @Inject
  var statisticsCalculators: Instance[StatisticsCalculator] = _

  def spendRegistered(@Observes(during = TransactionPhase.AFTER_SUCCESS) spend: Spend) = {
    statisticsCalculators.forEach(new Consumer[StatisticsCalculator] {
      override def accept(calculator: StatisticsCalculator): Unit = {
        calculator.recalculateForSpendData(spend.getCategory, spend.getAmount, spend.getCreated.toLocalDate)
      }
    })
  }

}

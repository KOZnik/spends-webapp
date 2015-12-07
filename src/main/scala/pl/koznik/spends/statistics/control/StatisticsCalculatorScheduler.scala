package pl.koznik.spends.statistics.control

import java.util.function.Consumer
import javax.ejb._
import javax.enterprise.inject.Instance
import javax.inject.Inject

import pl.koznik.spends.core.control.SpendsRepository

@Singleton
class StatisticsCalculatorScheduler {

  @Inject
  var statisticsCalculators: Instance[StatisticsCalculator] = _
  @Inject
  var spendsRepository: SpendsRepository = _

  @Schedule(minute = "*/1", hour = "*")
  def recalculateStatisticsForUnprocessedSpends() = {
    val unprocessedSpends = spendsRepository.unprocessedSpends()
    statisticsCalculators.forEach(new Consumer[StatisticsCalculator] {
      override def accept(calculator: StatisticsCalculator): Unit = {
        calculator.recalculateForSpendData(unprocessedSpends)
      }
    })
    unprocessedSpends.foreach(_.setProcessed(true))
  }

}

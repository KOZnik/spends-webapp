package pl.koznik.spends.statistics.control

import java.time.LocalDate
import javax.inject.Inject

import pl.koznik.spends.common.control.Converters._
import pl.koznik.spends.common.control.LocalDateTimeCalculations
import pl.koznik.spends.core.entity.Category.Category
import pl.koznik.spends.statistics.entity.MonthCategoryStatistics

class MonthCategoryStatisticsCalculator extends StatisticsCalculator {

  @Inject
  def this(repository: MonthCategoryStatisticsRepository) = {
    this
    this.repository = repository
  }

  var repository: MonthCategoryStatisticsRepository = _

  override def recalculateForSpendData(category: Category, amount: Double, date: LocalDate): Unit = {
    val beginOfMonth = date.withDayOfMonth(1)
    repository.byCategory(category) match {
      case Some(stat) => repository.update(new ForStatistics(stat).recalculateMonthAmount(amount, beginOfMonth).recalculateAverageMonthAmount(beginOfMonth).andGet())
      case None => repository.create(new MonthCategoryStatistics(category = category, amounts = Map(beginOfMonth -> amount))) //average for new category is 0
    }
  }

  class ForStatistics(var stat: MonthCategoryStatistics) {
    def recalculateMonthAmount(amount: Double, month: LocalDate): ForStatistics = {
      stat.getAmounts.put(month, amount + Option.apply(stat.getAmounts.get(month)).getOrElse(0.0).asInstanceOf[Double])
      this
    }

    def recalculateAverageMonthAmount(month: LocalDate): ForStatistics = {
      if (LocalDateTimeCalculations.dateNotInCurrentMonth(month)) {
        //average month amount recalculation needed
        val sum = stat.getAmounts.entrySet().filter(entry => LocalDateTimeCalculations.dateNotInCurrentMonth(entry.getKey)).map(_.getValue).foldLeft(0.0)(_ + _)
        stat.setAverageMonthAmount(sum / stat.getAmounts.entrySet().count(entry => LocalDateTimeCalculations.dateNotInCurrentMonth(entry.getKey)))
      }
      this
    }

    def andGet(): MonthCategoryStatistics = stat
  }

}

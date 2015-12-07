package pl.koznik.spends.statistics.entity

import java.time.LocalDate
import java.util
import javax.persistence._

import pl.koznik.spends.common.control.Converters._
import pl.koznik.spends.common.control.LocalDateTimeCalculations
import pl.koznik.spends.core.entity.{Category, Spend}
import pl.koznik.spends.statistics.control.{MonthCategoryStatisticsRepository, StatisticsCalculator}

import scala.beans.BeanProperty
import scala.collection.JavaConversions

@Entity
@Table(name = "month_category_statistics")
@NamedQueries(Array(
  new NamedQuery(name = "MonthCategoryStatistics.all", query = "SELECT s FROM MonthCategoryStatistics s"),
  new NamedQuery(name = "MonthCategoryStatistics.byCategory", query = "SELECT s FROM MonthCategoryStatistics s WHERE s.category = :category")
))
class MonthCategoryStatistics {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @BeanProperty
  var id: Long = _

  @BeanProperty
  var category: Category.Category = _

  @Version
  @BeanProperty
  var version: Long = _

  @BeanProperty
  @Column(name = "average_month_amount")
  var averageMonthAmount: Double = _

  @BeanProperty
  @ElementCollection
  @MapKeyColumn(name = "date")
  @MapKeyTemporal(TemporalType.DATE)
  @Column(name = "amount")
  @CollectionTable(name = "month_category_statistics_amount", joinColumns = Array(new JoinColumn(name = "id")))
  var amounts: java.util.Map[LocalDate, java.lang.Double] = _

  def this(category: Category.Category, averageMonthAmount: Double = 0, amounts: Map[LocalDate, java.lang.Double]) = {
    this
    this.category = category
    this.averageMonthAmount = averageMonthAmount
    this.amounts = new util.HashMap(JavaConversions.mapAsJavaMap(amounts))
  }

  def apply(spends: List[Spend]) = {
    spends.foreach(spend => {
      recalculateMonthAmount(spend.amount, spend.created.toLocalDate.withDayOfMonth(1))
      if (LocalDateTimeCalculations.dateNotInCurrentMonth(spend.created.toLocalDate.withDayOfMonth(1))) {
        //average month amount recalculation needed
        recalculateAverageMonthAmount()
      }
    })
  }

  private def recalculateMonthAmount(amount: Double, month: LocalDate) = {
    amounts.put(month, amount + Option.apply(amounts.get(month)).getOrElse(0.0).asInstanceOf[Double])
  }

  private def recalculateAverageMonthAmount() = {
    val sum = amounts.entrySet().filter(entry => LocalDateTimeCalculations.dateNotInCurrentMonth(entry.getKey)).map(_.getValue).foldLeft(0.0)(_ + _)
    averageMonthAmount = sum / amounts.entrySet().count(entry => LocalDateTimeCalculations.dateNotInCurrentMonth(entry.getKey))
  }
}

object MonthCategoryStatistics extends StatisticsCalculator {
  val ALL_CATEGORIES_STATISTICS = "MonthCategoryStatistics.all"
  val CATEGORY_STATISTICS = "MonthCategoryStatistics.byCategory"

  def recalculateForSpendData(spends: List[Spend]): Unit = {
    recalculateForSpendData(MonthCategoryStatisticsRepository.lookup(), spends)
  }

  def recalculateForSpendData(repository: MonthCategoryStatisticsRepository, spends: List[Spend]): Unit = {
    val spendsByCategory = spends.groupBy(_.category)
    spendsByCategory.keysIterator.foreach(category =>
      repository.byCategory(category) match {
        case Some(stat) => stat.apply(spendsByCategory.get(category).get)
        case None => repository.create(new MonthCategoryStatistics(category = category, amounts = Map.empty))
          .apply(spendsByCategory.get(category).get)
      }
    )
  }

}
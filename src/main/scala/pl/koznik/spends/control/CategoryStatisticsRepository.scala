package pl.koznik.spends.control

import java.time.LocalDate
import javax.ejb.{LocalBean, Stateless}
import javax.persistence.EntityManager

import pl.koznik.spends.control.Converters._
import pl.koznik.spends.entity.Category.Category
import pl.koznik.spends.entity.CategoryStatistics

@Stateless
@LocalBean
class CategoryStatisticsRepository extends CrudEjb[CategoryStatistics] {

  def this(manager: EntityManager) = {
    this
    this.manager = manager
  }

  def all(): List[CategoryStatistics] = findByNamedQuery(CategoryStatistics.ALL_CATEGORIES_STATISTICS)

  def categoryStatistics(category: Category): Option[CategoryStatistics] =
    findOneByNamedQuery(CategoryStatistics.CATEGORY_STATISTICS, Map("category" -> category))

  def dateNotInCurrentMonth(date: LocalDate): Boolean = !date.withDayOfMonth(1).equals(LocalDate.now().withDayOfMonth(1))

  def recalculatedStatisticsFor(stat: CategoryStatistics, amount: Double, date: LocalDate): CategoryStatistics = {
    stat.getAmounts.put(date, amount + Option.apply(stat.getAmounts.get(date)).getOrElse(0.0).asInstanceOf[Double])
    if (dateNotInCurrentMonth(date)) {
      //average month amount recalculation needed
      val sum = stat.getAmounts.entrySet().filter(entry => dateNotInCurrentMonth(entry.getKey)).map(_.getValue).foldLeft(0.0)(_ + _)
      stat.setAverageMonthAmount(sum / stat.getAmounts.entrySet().count(entry => dateNotInCurrentMonth(entry.getKey)))
    }
    stat
  }

  def updateStatistics(category: Category, amount: Double, date: LocalDate) = {
    val beginOfMonth = date.withDayOfMonth(1)
    categoryStatistics(category) match {
      case Some(stat) => update(recalculatedStatisticsFor(stat, amount, beginOfMonth))
      case None => create(new CategoryStatistics(category = category, amounts = Map(beginOfMonth -> amount))) //average for new category is 0
    }
  }

}

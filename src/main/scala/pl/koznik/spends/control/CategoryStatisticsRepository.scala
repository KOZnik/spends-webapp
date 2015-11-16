package pl.koznik.spends.control

import java.time.{LocalDate, LocalDateTime}
import java.util.function.BiFunction
import javax.ejb.{LocalBean, Stateless}
import javax.persistence.EntityManager

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

  def recomputedStatisticsFor(stat: CategoryStatistics, amount: Double, date: LocalDateTime): CategoryStatistics = {
    if (stat.getAmounts.containsKey(date.toLocalDate)) {
      stat.getAmounts.compute(date.toLocalDate, func)
    } else {

    }
  }

  val func = new BiFunction[_ >: LocalDate, _ >: Double, _ <: Double] {
    override def apply(t: LocalDate, u: Double): Double = {
      10.0
    }
  }

  def updateStatistics(category: Category, amount: Double, date: LocalDateTime) = {
    categoryStatistics(category) match {
      case Some(stat) => update(recomputedStatisticsFor(stat, amount, date))
      case None =>
    }
  }

}

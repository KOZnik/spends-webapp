package pl.koznik.spends.statistics.control

import javax.ejb.{LocalBean, Stateless}
import javax.persistence.EntityManager

import pl.koznik.spends.common.control.CrudEjb
import pl.koznik.spends.core.entity.Category.Category
import pl.koznik.spends.statistics.entity.MonthCategoryStatistics

@Stateless
@LocalBean
class MonthCategoryStatisticsRepository extends CrudEjb[MonthCategoryStatistics] {

  def this(manager: EntityManager) = {
    this
    this.manager = manager
  }

  def all(): List[MonthCategoryStatistics] = findByNamedQuery(MonthCategoryStatistics.ALL_CATEGORIES_STATISTICS)

  def byCategory(category: Category): Option[MonthCategoryStatistics] =
    findOneByNamedQuery(MonthCategoryStatistics.CATEGORY_STATISTICS, Map("category" -> category))

}

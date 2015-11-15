package pl.koznik.spends.control

import javax.ejb.{LocalBean, Stateless}
import javax.persistence.EntityManager

import pl.koznik.spends.entity.CategoryStatistics

@Stateless
@LocalBean
class CategoryStatisticsRepository extends CrudEjb[CategoryStatistics] {

  def this(manager: EntityManager) = {
    this
    this.manager = manager
  }

}

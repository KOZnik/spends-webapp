package pl.koznik.spends.control

import javax.ejb.{LocalBean, Stateless}
import javax.persistence.EntityManager

import pl.koznik.spends.entity.Spend

@Stateless
@LocalBean
class SpendsRepository extends CrudEjb[Spend] {

  def this(manager: EntityManager) = {
    this
    this.manager = manager
  }

  def spendForMonth(year: Int, month: Int): java.util.List[Spend] =
    findByNamedQuery(Constants.SPENDS_IN_MONTH,
      Map("monthBeginDate" -> LocalDateTimeCalculator.beginOfMonth(year, month), "monthEndDate" -> LocalDateTimeCalculator.endOfMonth(year, month)))

}

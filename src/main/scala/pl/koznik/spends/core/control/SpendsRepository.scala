package pl.koznik.spends.core.control

import javax.ejb.{LocalBean, Stateless}
import javax.persistence.EntityManager

import pl.koznik.spends.common.control.{CrudEjb, LocalDateTimeCalculations}
import pl.koznik.spends.core.entity.Spend

@Stateless
@LocalBean
class SpendsRepository extends CrudEjb[Spend] {

  def this(manager: EntityManager) = {
    this
    this.manager = manager
  }

  def spendForMonth(year: Int, month: Int): List[Spend] =
    findByNamedQuery(Spend.SPENDS_IN_MONTH,
      Map("monthBeginDate" -> LocalDateTimeCalculations.beginOfMonth(year, month), "monthEndDate" -> LocalDateTimeCalculations.endOfMonth(year, month)))

  def unprocessedSpends(): List[Spend] = findByNamedQuery(Spend.UNPROCESSED_SPENDS)

}

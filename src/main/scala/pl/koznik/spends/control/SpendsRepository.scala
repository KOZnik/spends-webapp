package pl.koznik.spends.control

import javax.ejb.{LocalBean, Stateless}

import pl.koznik.spends.entity.Spend

@Stateless
@LocalBean
class SpendsRepository extends CrudEjb[Spend] {

  def spendForMonth(year: Int, month: Int): java.util.List[Spend] =
    findByNamedQuery(Constants.SPENDS_IN_MONTH,
      Map("monthBeginDate" -> LocalDateTimeCalculator.beginOfMonth(year, month), "monthEndDate" -> LocalDateTimeCalculator.endOfMonth(year, month)))

}

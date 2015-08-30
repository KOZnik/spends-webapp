package pl.koznik.spends.control

import javax.ejb.{LocalBean, Stateless}

import pl.koznik.spends.entity.Spend

@Stateless
@LocalBean
class SpendsRepository extends CrudEjb[Spend] {

}

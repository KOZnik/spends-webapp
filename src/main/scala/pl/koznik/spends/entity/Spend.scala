package pl.koznik.spends.entity

import javax.persistence._

import scala.beans.BeanProperty

@Entity
@NamedQuery(name = "findAllSpends", query = "SELECT s FROM Spend s")
class Spend {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @BeanProperty
  var id: Long = _

}
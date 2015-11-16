package pl.koznik.spends.entity

import java.time.LocalDateTime
import javax.persistence._

import pl.koznik.spends.entity.Category.Category

import scala.beans.BeanProperty

@Entity
@NamedQuery(name = "Spend.spendsInMonth", query = "SELECT s FROM Spend s WHERE s.created BETWEEN :monthBeginDate AND :monthEndDate")
class Spend {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @BeanProperty
  var id: Long = _

  @Temporal(TemporalType.TIMESTAMP)
  @BeanProperty
  var created: LocalDateTime = _

  @BeanProperty
  var category: Category = _

  @BeanProperty
  var amount: Double = _

  @BeanProperty
  var description: String = _

  def this(created: LocalDateTime, category: Category, amount: Double, description: String = "") = {
    this
    this.created = created
    this.category = category
    this.amount = amount
    this.description = description
  }

}

object Spend {
  val SPENDS_IN_MONTH = "Spend.spendsInMonth"
}
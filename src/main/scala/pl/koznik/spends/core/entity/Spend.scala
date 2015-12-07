package pl.koznik.spends.core.entity

import java.time.LocalDateTime
import javax.persistence._

import Category._

import scala.beans.BeanProperty

@Entity
@NamedQueries(Array(
  new NamedQuery(name = "Spend.spendsInMonth", query = "SELECT s FROM Spend s WHERE s.created BETWEEN :monthBeginDate AND :monthEndDate"),
  new NamedQuery(name = "Spend.unprocessed", query = "SELECT s FROM Spend s WHERE s.processed = false")
))
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

  @Version
  @BeanProperty
  var version: Long = _

  @BeanProperty
  var processed: Boolean = _

  def this(created: LocalDateTime, category: Category, amount: Double, description: String = "", processed: Boolean = false) = {
    this
    this.created = created
    this.category = category
    this.amount = amount
    this.description = description
    this.processed = processed
  }

}

object Spend {
  val SPENDS_IN_MONTH = "Spend.spendsInMonth"
  val UNPROCESSED_SPENDS = "Spend.unprocessed"
}
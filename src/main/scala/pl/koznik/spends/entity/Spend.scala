package pl.koznik.spends.entity

import java.time.LocalDateTime
import javax.persistence._

import pl.koznik.spends.entity.Category.Category

import scala.beans.BeanProperty

@Entity
@NamedQuery(name = "spendsInMonth", query = "SELECT s FROM Spend s WHERE s.created BETWEEN :monthBeginDate AND :monthEndDate")
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

  def this(created: LocalDateTime, category: Category, amount: Double) = {
    this
    this.created = created
    this.category = category
    this.amount = amount
  }

}

object Category extends Enumeration {
  type Category = Value
  val FOOD, MY, WIFE, SON, CAR, TAXES, DIFFERENT, UNKNOWN = Value

  def forName(name: String) = if (values.exists(_.toString == name)) Option.apply(withName(name)) else Option.empty

}
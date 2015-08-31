package pl.koznik.spends.entity

import java.time.LocalDateTime
import javax.persistence._

import pl.koznik.spends.entity.Category.Category

import scala.beans.BeanProperty

@Entity
@NamedQuery(name = "findAllSpends", query = "SELECT s FROM Spend s")
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

  //TODO add amount

  def this(created: LocalDateTime, category: Category) = {
    this
    this.created = created
    this.category = category
  }

}

object Category extends Enumeration {
  type Category = Value
  val FOOD, MY, WIFE, SON, CAR, TAXES, DIFFERENT = Value
}
package pl.koznik.spends.core.boundary

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.validation.constraints.{Min, Pattern}
import javax.xml.bind.annotation.XmlRootElement

import pl.koznik.spends.core.entity._

import scala.beans.BeanProperty

@XmlRootElement
class SpendRequest {

  @Pattern(regexp = "FOOD|MY|WIFE|SON|CAR|TAXES|DIFFERENT|UNKNOWN")
  @BeanProperty
  var category: String = _

  @BeanProperty
  var description: String = _

  @Min(1)
  @BeanProperty
  var amount: Double = _

  @Pattern(regexp = "^((19|20)\\d\\d)-(0?[1-9]|1[012])-(0?[1-9]|[12][0-9]|3[01])$")
  @BeanProperty
  var created: String = _

  def this(category: String, description: String = "", amount: Double, created: String) = {
    this
    this.description = description
    this.category = category
    this.amount = amount
    this.created = created
  }

  def toSpend(): Spend = {
    new Spend(created = LocalDate.parse(created, DateTimeFormatter.ofPattern("yyyy-MM-dd")).atStartOfDay(),
      category = Category.forName(category).getOrElse(Category.UNKNOWN),
      amount = amount,
      description = description,
      processed = false)
  }

  override def toString = s"SpendRequest($category, $description, $amount, $created)"
}
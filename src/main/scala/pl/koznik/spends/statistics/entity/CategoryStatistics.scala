package pl.koznik.spends.statistics.entity

import java.time.LocalDate
import java.util
import javax.persistence._

import pl.koznik.spends.core.entity.Category

import scala.beans.BeanProperty
import scala.collection.JavaConversions

@Entity
@Table(name = "category_statistics")
@NamedQueries(Array(
  new NamedQuery(name = "CategoryStatistics.all", query = "SELECT s FROM CategoryStatistics s"),
  new NamedQuery(name = "CategoryStatistics.byCategory", query = "SELECT s FROM CategoryStatistics s WHERE s.category = :category")
))
class CategoryStatistics {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @BeanProperty
  var id: Long = _

  @BeanProperty
  var category: Category.Category = _

  @Version
  @BeanProperty
  var version: Long = _

  @BeanProperty
  @Column(name = "average_month_amount")
  var averageMonthAmount: Double = _

  @BeanProperty
  @ElementCollection
  @MapKeyColumn(name = "date")
  @Column(name = "amount")
  @CollectionTable(name = "category_statistics_amount", joinColumns = Array(new JoinColumn(name = "id")))
  var amounts: java.util.Map[LocalDate, java.lang.Double] = _

  def this(category: Category.Category, averageMonthAmount: Double = 0, amounts: Map[LocalDate, java.lang.Double]) = {
    this
    this.category = category
    this.averageMonthAmount = averageMonthAmount
    this.amounts = new util.HashMap(JavaConversions.mapAsJavaMap(amounts))
  }

}

object CategoryStatistics {
  val ALL_CATEGORIES_STATISTICS = "CategoryStatistics.all"
  val CATEGORY_STATISTICS = "CategoryStatistics.byCategory"
}
package pl.koznik.spends.entity

import java.time.LocalDate
import javax.persistence._

import scala.beans.BeanProperty

@Entity
@Table(name = "category_statistics")
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

}

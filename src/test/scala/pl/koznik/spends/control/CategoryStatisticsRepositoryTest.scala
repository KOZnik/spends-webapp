package pl.koznik.spends.control

import java.time.LocalDate

import org.scalatest.{Matchers, FlatSpec}
import pl.koznik.configuration.DatabaseTest
import pl.koznik.spends.entity.{Category, CategoryStatistics}

import scala.collection.JavaConverters

class CategoryStatisticsRepositoryTest extends FlatSpec with Matchers with DatabaseTest {

  val repository = new CategoryStatisticsRepository(entityManager)

  "Repository" should "persist spends" in {
    transaction.begin()
    var stat = new CategoryStatistics
    stat.setCategory(Category.CAR)
    stat.setAmounts(JavaConverters.mapAsJavaMapConverter(Map(LocalDate.now() -> java.lang.Double.valueOf(100.0))).asJava)
    repository.create(stat)
    transaction.commit()
  }
}

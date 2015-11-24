package pl.koznik.spends.statistics.control

import java.time.LocalDate

import org.scalatest.{FlatSpec, Matchers}
import pl.koznik.spends.common.control.DatabaseTest
import pl.koznik.spends.core.entity.Category

class MonthCategoryStatisticsCalculatorTest extends FlatSpec with Matchers with DatabaseTest {

  val repository = new MonthCategoryStatisticsRepository(entityManager)
  val calculator = new MonthCategoryStatisticsCalculator(repository)

  "Repository" should "persist statistics for new category" in {
    transactional { () =>
      calculator.recalculateForSpendData(Category.CAR, 10, LocalDate.now())
      val statistics = repository.byCategory(Category.CAR)

      statistics shouldBe defined
      statistics.get should have(
        'category(Category.CAR)
      )
      statistics.get.amounts should contain key dateForCurrentMonth
    }
  }

  it should "update month amount for existing category" in {
    transactional { () =>
      calculator.recalculateForSpendData(Category.CAR, 10, LocalDate.now())
      val statistics = repository.byCategory(Category.CAR).get

      statistics.amounts.get(dateForCurrentMonth()) should be(20)
    }
  }

  it should "calculate average month amount for previous months only" in {
    transactional { () =>
      calculator.recalculateForSpendData(Category.CAR, 33, LocalDate.of(2010, 1, 1))
      calculator.recalculateForSpendData(Category.CAR, 35, LocalDate.of(2010, 1, 15))
      calculator.recalculateForSpendData(Category.CAR, 12, LocalDate.of(2010, 2, 1))
      calculator.recalculateForSpendData(Category.FOOD, 100, LocalDate.of(2010, 2, 1))
      val statistics = repository.byCategory(Category.CAR).get

      statistics.averageMonthAmount should be((33 + 35 + 12) / 2)
    }
  }

  def dateForCurrentMonth(): LocalDate = LocalDate.now().withDayOfMonth(1)
}

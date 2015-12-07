package pl.koznik.spends.statistics.entity

import java.time.{LocalDate, LocalDateTime}

import org.scalatest.{FlatSpec, Matchers}
import pl.koznik.spends.common.control.DatabaseTest
import pl.koznik.spends.core.entity.{Category, Spend}
import pl.koznik.spends.statistics.control.MonthCategoryStatisticsRepository

class MonthCategoryStatisticsTest extends FlatSpec with Matchers with DatabaseTest {

  val repository = new MonthCategoryStatisticsRepository(entityManager)

  "Statistics" should "create statistics for new spend category" in {
    transactional { () =>
      MonthCategoryStatistics.recalculateForSpendData(repository, List.apply(new Spend(LocalDateTime.now(), Category.CAR, 10)))
      val statistics = repository.byCategory(Category.CAR)

      statistics shouldBe defined
      statistics.get should have(
        'category(Category.CAR)
      )
      statistics.get.amounts should contain key dateForCurrentMonth
    }
  }

  it should "update month amount for spends with existing category statistics" in {
    transactional { () =>
      MonthCategoryStatistics.recalculateForSpendData(repository, List.apply(new Spend(LocalDateTime.now(), Category.CAR, 10)))
      val statistics = repository.byCategory(Category.CAR).get

      statistics.amounts.get(dateForCurrentMonth()) should be(20)
    }
  }

  it should "calculate average month amount for previous months only" in {
    transactional { () =>
      MonthCategoryStatistics.recalculateForSpendData(repository, List.apply(new Spend(LocalDateTime.of(2010, 1, 1, 0, 0), Category.CAR, 33)))
      MonthCategoryStatistics.recalculateForSpendData(repository, List.apply(new Spend(LocalDateTime.of(2010, 1, 15, 0, 0), Category.CAR, 35)))
      MonthCategoryStatistics.recalculateForSpendData(repository, List.apply(new Spend(LocalDateTime.of(2010, 2, 1, 0, 0), Category.CAR, 12)))
      MonthCategoryStatistics.recalculateForSpendData(repository, List.apply(new Spend(LocalDateTime.of(2010, 2, 1, 0, 0), Category.FOOD, 100)))
      val statistics = repository.byCategory(Category.CAR).get

      statistics.averageMonthAmount should be((33 + 35 + 12) / 2)
    }
  }

  def dateForCurrentMonth(): LocalDate = LocalDate.now().withDayOfMonth(1)
}

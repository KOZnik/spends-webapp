package pl.koznik.spends.control

import java.time.{DateTimeException, LocalDateTime}

import org.scalatest.Matchers
import pl.koznik.configuration.DatabaseTest
import pl.koznik.spends.entity.{Category, Spend}

class SpendsRepositoryTest extends org.scalatest.FlatSpec with Matchers with DatabaseTest {

  val repository = new SpendsRepository(entityManager)
  val actualDateTime = LocalDateTime.now()

  "Repository" should "persist spends" in {
    transactional { () =>
      repository.create(new Spend(actualDateTime, Category.CAR, 1))
      repository.create(new Spend(actualDateTime, Category.DIFFERENT, 50.1))
    }
  }

  it should "find persisted spends for month that spends were entered" in {
    repository.spendForMonth(actualDateTime.getYear, actualDateTime.getMonthValue) should have size 2
  }

  it should "find no spends for month without spends entered" in {
    repository.spendForMonth(2014, 1) should be('empty)
  }

  it should "throw exception for broken date specified" in {
    an[DateTimeException] should be thrownBy repository.spendForMonth(2014, 32)
  }

}

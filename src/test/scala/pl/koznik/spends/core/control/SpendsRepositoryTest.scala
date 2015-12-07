package pl.koznik.spends.core.control

import java.time.{DateTimeException, LocalDateTime}

import org.scalatest.Matchers
import pl.koznik.spends.common.control.DatabaseTest
import pl.koznik.spends.core.entity.{Category, Spend}

class SpendsRepositoryTest extends org.scalatest.FlatSpec with Matchers with DatabaseTest {

  var repository = new SpendsRepository(entityManager)
  val actualDateTime = LocalDateTime.now()

  "Repository" should "persist spends" in {
    transactional { () =>
      repository.create(new Spend(actualDateTime, Category.CAR, 1))
      repository.create(new Spend(actualDateTime, Category.DIFFERENT, 50.1))
    }
  }

  it should "find persisted spends for month that spends were entered" in {
    transactional { () =>
      repository.spendForMonth(actualDateTime.getYear, actualDateTime.getMonthValue) should have size 2
    }
  }

  it should "find no spends for month without spends entered" in {
    transactional { () =>
      repository.spendForMonth(2014, 1) should be('empty)
    }
  }

  it should "throw exception for broken date specified" in {
    transactional { () =>
      an[DateTimeException] should be thrownBy repository.spendForMonth(2014, 32)
    }
  }

  it should "find new spends as unprocessed by statistics calculators" in {
    transactional { () =>
      repository.unprocessedSpends() should have size 2
    }
  }

}

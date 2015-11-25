package pl.koznik.spends.core.control

import java.lang.annotation.Annotation
import java.time.{DateTimeException, LocalDateTime}
import javax.enterprise.event.Event
import javax.enterprise.util.TypeLiteral

import org.scalatest.Matchers
import pl.koznik.spends.common.control.DatabaseTest
import pl.koznik.spends.core.entity.{Category, Spend}

class SpendsRepositoryTest extends org.scalatest.FlatSpec with Matchers with DatabaseTest {

  var repository = new SpendsRepository(entityManager)
  val actualDateTime = LocalDateTime.now()

  "Repository" should "persist spends" in {
    repository.events = eventsFake
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

  val eventsFake = new Event[Spend] {
    override def fire(event: Spend): Unit = {}

    override def select(qualifiers: Annotation*): Event[Spend] = ???

    override def select[U <: Spend](subtype: Class[U], qualifiers: Annotation*): Event[U] = ???

    override def select[U <: Spend](subtype: TypeLiteral[U], qualifiers: Annotation*): Event[U] = ???
  }

}

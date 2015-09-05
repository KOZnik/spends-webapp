package pl.koznik.spends.boundary

import java.time.LocalDateTime
import javax.inject.Inject
import javax.ws.rs._
import javax.ws.rs.core.{MediaType, Response}
import javax.xml.bind.annotation.XmlRootElement

import pl.koznik.spends.control.Converters._
import pl.koznik.spends.control.SpendsRepository
import pl.koznik.spends.entity.{Category, Spend}

import scala.beans.BeanProperty
import scala.collection.JavaConversions

@Path("spends")
@Produces(Array(MediaType.APPLICATION_JSON))
class SpendsResource {

  @Inject
  var spendsRepository: SpendsRepository = _

  @GET
  def all(@QueryParam("forYear") forYear: Int, @QueryParam("forMonth") forMonth: Int): java.util.Map[String, java.util.List[SpendResponse]] = {
    JavaConversions.mapAsJavaMap(
      spendsRepository.spendForMonth(forYear, forMonth)
        .groupBy(spend => spend.getCategory.toString)
        .mapValues(list => list.map(spend => new SpendResponse(spend.getCreated, spend.getAmount)))
    )
  }

  @XmlRootElement
  class SpendResponse(@BeanProperty val created: String, @BeanProperty val amount: Double)

  @GET
  @Path("categories")
  def allCategories(): java.util.Set[String] = JavaConversions.setAsJavaSet(Category.values.map(c => c.toString))

  @POST
  def add(@FormParam("category") categoryName: String, @FormParam("amount") amount: Double): Response = {
    val spend = new Spend(LocalDateTime.now(), Category.forName(categoryName).orElse(Option.apply(Category.UNKNOWN)).get, amount)
    spendsRepository create spend
    Response.ok().build()
  }

}

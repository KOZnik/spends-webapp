package pl.koznik.spends.core.boundary

import java.time.{LocalDateTime, ZoneOffset}
import javax.inject.Inject
import javax.validation.constraints.NotNull
import javax.ws.rs._
import javax.ws.rs.core.{MediaType, Response}
import javax.xml.bind.annotation.XmlRootElement

import com.typesafe.config.{Config, ConfigFactory}
import pl.koznik.spends.common.control.Converters._
import pl.koznik.spends.core.control.SpendsRepository
import pl.koznik.spends.core.entity.{Category, Spend}

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
        .groupBy(_.getCategory.toString)
        .mapValues(list => list.map(spend => new SpendResponse(spend.getCreated.toInstant(ZoneOffset.ofTotalSeconds(60 * 60)).toEpochMilli, spend.getDescription, spend.getAmount)))
    )
  }

  @XmlRootElement
  class SpendResponse(@BeanProperty val created: Long, @BeanProperty val description: String, @BeanProperty val amount: Double)

  @GET
  @Path("categories")
  def allCategoriesKeys(): java.util.List[String] = {
    Category.values.map(_.toString) toList
  }

  @POST
  def add(@FormParam("category") categoryName: String,
          @NotNull @FormParam("amount") amount: Double,
          @FormParam("description") description: String,
          @FormParam("date") date: String): Response = {
    val spend = new Spend(Option.apply(date).map(LocalDateTime.parse(_)).getOrElse(LocalDateTime.now()),
      Category.forName(categoryName).orElse(Option.apply(Category.UNKNOWN)).get,
      amount, description)
    spendsRepository create spend
    Response.ok().build()
  }

}

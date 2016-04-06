package pl.koznik.spends.core.boundary

import java.time.ZoneOffset
import javax.inject.Inject
import javax.validation.Valid
import javax.ws.rs._
import javax.ws.rs.core.{MediaType, Response}
import javax.xml.bind.annotation.XmlRootElement

import pl.koznik.spends.common.control.Converters._
import pl.koznik.spends.core.control.SpendsRepository
import pl.koznik.spends.core.entity.Category

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
  class SpendResponse(@BeanProperty val created: Long, @BeanProperty val description: String, @BeanProperty val amount: Double, @BeanProperty val category: String = "")

  @GET
  @Path("categories")
  def allCategoriesKeys(): java.util.List[String] = {
    Category.values.map(_.toString) toList
  }

  @POST
  @Consumes(Array(MediaType.APPLICATION_JSON))
  def add(@Valid spendRequest: SpendRequest): Response = {
    val spend = spendsRepository create spendRequest.toSpend()
    Response.ok(
      new SpendResponse(spend.getCreated.toInstant(ZoneOffset.ofTotalSeconds(60 * 60)).toEpochMilli, spend.getDescription, spend.getAmount, spend.getCategory.toString)
    ).build()
  }

}

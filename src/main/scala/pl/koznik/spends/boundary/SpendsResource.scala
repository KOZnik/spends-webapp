package pl.koznik.spends.boundary

import java.time.LocalDateTime
import javax.ejb.Stateless
import javax.inject.Inject
import javax.ws.rs._
import javax.ws.rs.core.{MediaType, Response}
import javax.xml.bind.annotation.XmlRootElement

import pl.koznik.spends.control.Converters._
import pl.koznik.spends.control.{Constants, SpendsRepository}
import pl.koznik.spends.entity.{Category, Spend}

import scala.beans.BeanProperty
import scala.collection.JavaConversions

@Path("spends")
@Stateless
class SpendsResource {

  @Inject
  var spendsRepository: SpendsRepository = _

  @GET
  @Produces(Array(MediaType.APPLICATION_JSON))
  def all(): java.util.Map[String, java.util.List[SpendResponse]] = {
    JavaConversions.mapAsJavaMap(
      spendsRepository.findByNamedQuery(Constants.FIND_ALL_SPENDS_QUERY)
        .groupBy(spend => spend.getCategory.toString)
        .mapValues(list => list.map(spend => new SpendResponse(spend.getCreated, spend.getAmount)))
    )
  }

  @XmlRootElement
  class SpendResponse(@BeanProperty val created: String, @BeanProperty val amount: Double)

  @GET
  @Path("categories")
  @Produces(Array(MediaType.APPLICATION_JSON))
  def allCategories(): java.util.Set[String] = JavaConversions.setAsJavaSet(Category.values.map(c => c.toString))

  @POST
  def add(@FormParam("category") categoryName: String, @FormParam("amount") amount: Double): Response = {
    val spend = new Spend(LocalDateTime.now(), Category.forName(categoryName).orElse(Option.apply(Category.UNKNOWN)).get, amount)
    spendsRepository create spend
    Response.ok().build()
  }

}

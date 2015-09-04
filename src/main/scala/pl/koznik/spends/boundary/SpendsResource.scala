package pl.koznik.spends.boundary

import java.time.LocalDateTime
import javax.ejb.Stateless
import javax.inject.Inject
import javax.json.{Json, JsonObject}
import javax.ws.rs._
import javax.ws.rs.core.{MediaType, Response}

import pl.koznik.spends.control.Converters._
import pl.koznik.spends.control.{Constants, SpendsRepository}
import pl.koznik.spends.entity.{Category, Spend}

@Path("spends")
@Stateless
class SpendsResource {

  @Inject
  var spendsRepository: SpendsRepository = _

  @GET
  @Produces(Array(MediaType.APPLICATION_JSON))
  def all(): List[JsonObject] = {
    spendsRepository.findByNamedQuery(Constants.FIND_ALL_SPENDS_QUERY)
      .map((spend: Spend) =>
      Json.createObjectBuilder()
        .add("created", spend.getCreated)
        .add("category", spend.getCategory.toString)
        .add("amount", spend.getAmount)
        .build())
      .toList
  }

  @GET
  @Path("categories")
  @Produces(Array(MediaType.APPLICATION_JSON))
  def allCategories(): Set[String] = Category.values.map(c => c.toString)

  @POST
  def add(@FormParam("category") categoryName: String, @FormParam("amount") amount: Double): Response = {
    val spend = new Spend(LocalDateTime.now(), Category.forName(categoryName).orElse(Option.apply(Category.UNKNOWN)).get, amount)
    spendsRepository create spend
    Response.ok().build()
  }

}

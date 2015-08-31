package pl.koznik.spends.boundary

import java.time.LocalDateTime
import javax.ejb.Stateless
import javax.inject.Inject
import javax.json.{Json, JsonObject}
import javax.ws.rs.{GET, POST, Path}

import pl.koznik.spends.control.Converters._
import pl.koznik.spends.control.{Constants, SpendsRepository}
import pl.koznik.spends.entity.{Category, Spend}

@Path("spends")
@Stateless
class SpendsResource {

  @Inject
  var spendsRepository: SpendsRepository = _

  @GET
  def all(): List[JsonObject] = {
    spendsRepository.findByNamedQuery(Constants.FIND_ALL_SPENDS_QUERY)
      .map((spend: Spend) =>
      Json.createObjectBuilder()
        .add("created", spend.getCreated)
        .add("category", spend.getCategory.toString)
        .build())
      .toList
  }

  //TODO implement
  @POST
  def add(): Unit = {
    val spend = new Spend(LocalDateTime.now(), Category.CAR)
    spendsRepository create spend
  }

}

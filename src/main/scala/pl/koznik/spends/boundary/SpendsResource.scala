package pl.koznik.spends.boundary

import javax.ejb.Stateless
import javax.inject.Inject
import javax.json.{Json, JsonObject}
import javax.ws.rs.{GET, Path}

import pl.koznik.spends.control.{Constants, SpendsRepository}
import pl.koznik.spends.entity.Spend

import scala.collection.JavaConverters

@Path("spends")
@Stateless
class SpendsResource {

  @Inject
  var spendsRepository: SpendsRepository = _

  @GET
  def all(): List[JsonObject] = {
    spendsRepository.findByNamedQuery(Constants.FIND_ALL_SPENDS_QUERY)
      .map((spend: Spend) => Json.createObjectBuilder().add("id", spend.getId).build())
      .toList
  }

  implicit def collectionToScalaStream[E](collection: java.util.Collection[E]): Stream[E] = {
    JavaConverters.asScalaIteratorConverter(collection.iterator()).asScala.toStream
  }

}

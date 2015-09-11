package pl.koznik.spends.boundary

import javax.json.JsonObject
import javax.ws.rs.client.ClientBuilder
import javax.ws.rs.core.Response

import com.typesafe.config.{Config, ConfigFactory}
import org.glassfish.jersey.client.ClientConfig
import org.glassfish.jersey.jsonp.JsonProcessingFeature
import org.scalatest.{FlatSpec, Matchers}
import pl.koznik.spends.entity.Category

class SpendsResourceTest extends FlatSpec with Matchers {

  behavior of "SpendsResourceTest"

  val categoriesServiceInvocation = ClientBuilder.newClient(new ClientConfig().register(classOf[JsonProcessingFeature]))
    .target("http://localhost:8080/spends").path("resources/spends/categories").request().buildGet()

  it should "return all spend categories with their locale descriptions" in {
    checkIfServiceIsDeployed
    val response = categoriesServiceInvocation.invoke()
    val conf: Config = ConfigFactory.load()
    val responseJSON = response.readEntity(classOf[JsonObject])

    response.getStatusInfo.getFamily should be(Response.Status.OK.getFamily)
    Category.values.foreach(
      category => responseJSON.getString(category.toString) should equal(conf.getString("category." + category))
    )
  }

  def checkIfServiceIsDeployed = {
    try {
      ClientBuilder.newClient().target("http://localhost:8080").request().get()
    } catch {
      case e: Exception => cancel("Can't run the test because service is not deployed")
    }
  }

}

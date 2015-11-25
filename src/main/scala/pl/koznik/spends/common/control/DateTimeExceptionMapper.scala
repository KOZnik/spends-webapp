package pl.koznik.spends.common.control

import java.time.DateTimeException
import javax.json.Json
import javax.ws.rs.core.Response
import javax.ws.rs.ext.{ExceptionMapper, Provider}

@Provider
class DateTimeExceptionMapper extends ExceptionMapper[DateTimeException] {
  override def toResponse(e: DateTimeException): Response =
    Response.status(Response.Status.BAD_REQUEST).entity(Json.createObjectBuilder().add("errorMessage", "Wrong date specified").build()).build()
}

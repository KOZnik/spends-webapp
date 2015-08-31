package pl.koznik.spends.boundary

import java.io.{OutputStream, PrintStream}
import java.lang.annotation.Annotation
import java.lang.reflect.Type
import javax.json.{Json, JsonObject}
import javax.ws.rs.Produces
import javax.ws.rs.core.{MediaType, MultivaluedMap}
import javax.ws.rs.ext.{MessageBodyWriter, Provider}

@Provider
@Produces(Array(MediaType.APPLICATION_JSON))
class ScalaListJsonObjectBodyWriter extends MessageBodyWriter[List[JsonObject]] {

  override def writeTo(list: List[JsonObject], aClass: Class[_], `type`: Type, annotations: Array[Annotation], mediaType: MediaType, multivaluedMap: MultivaluedMap[String, AnyRef], outputStream: OutputStream): Unit = {
    val builder = Json.createArrayBuilder()
    list foreach (x => builder add x)
    new PrintStream(outputStream) print builder.build()
  }

  override def getSize(list: List[JsonObject], aClass: Class[_], `type`: Type, annotations: Array[Annotation], mediaType: MediaType): Long = -1

  override def isWriteable(aClass: Class[_], `type`: Type, annotations: Array[Annotation], mediaType: MediaType): Boolean = true
}

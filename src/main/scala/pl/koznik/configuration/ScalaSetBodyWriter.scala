package pl.koznik.configuration

import java.io.{OutputStream, PrintStream}
import java.lang.annotation.Annotation
import java.lang.reflect.Type
import javax.ws.rs.Produces
import javax.ws.rs.core.{MediaType, MultivaluedMap}
import javax.ws.rs.ext.{MessageBodyWriter, Provider}

@Provider
@Produces(Array(MediaType.APPLICATION_JSON))
class ScalaSetBodyWriter extends MessageBodyWriter[Set[_]] {
  override def writeTo(set: Set[_], aClass: Class[_], `type`: Type, annotations: Array[Annotation], mediaType: MediaType, multivaluedMap: MultivaluedMap[String, AnyRef], outputStream: OutputStream): Unit = {
    val builder = StringBuilder.newBuilder
    set foreach (x => builder.append(toJsonString(x) + ","))
    new PrintStream(outputStream) print ("[" + builder.dropRight(1) + "]")
  }

  def toJsonString(x : Any) : String = {
    if (x.isInstanceOf[String]) "\"" + x.toString + "\""
    else x.toString
  }

  override def getSize(t: Set[_], aClass: Class[_], `type`: Type, annotations: Array[Annotation], mediaType: MediaType): Long = -1

  override def isWriteable(aClass: Class[_], `type`: Type, annotations: Array[Annotation], mediaType: MediaType): Boolean = true
}

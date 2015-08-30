package pl.koznik.spends.boundary

import java.io.{PrintStream, OutputStream}
import java.lang.annotation.Annotation
import java.lang.reflect.Type
import javax.ws.rs.Produces
import javax.ws.rs.core.{MediaType, MultivaluedMap}
import javax.ws.rs.ext.{MessageBodyWriter, Provider}

@Provider
@Produces(Array(MediaType.APPLICATION_JSON))
class NilToJsonBodyWriter[Nil] extends MessageBodyWriter[Nil] {

  override def writeTo(t: Nil, aClass: Class[_], `type`: Type, annotations: Array[Annotation], mediaType: MediaType, multivaluedMap: MultivaluedMap[String, AnyRef], outputStream: OutputStream): Unit = {
    if (`type`.getTypeName contains "scala.collection.immutable.List")
      new PrintStream(outputStream) print "[]"
    else
      new PrintStream(outputStream) print ""
  }

  override def getSize(t: Nil, aClass: Class[_], `type`: Type, annotations: Array[Annotation], mediaType: MediaType): Long = -1

  override def isWriteable(aClass: Class[_], `type`: Type, annotations: Array[Annotation], mediaType: MediaType): Boolean = true
}

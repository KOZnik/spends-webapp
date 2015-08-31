package pl.koznik.spends.control

import java.time.format.DateTimeFormatter
import java.time.{LocalDateTime, ZoneId}
import java.util.Date
import javax.persistence.{AttributeConverter, Converter}

import scala.collection.JavaConverters

object Converters {

  implicit def collectionToScalaStream[E](collection: java.util.Collection[E]): Stream[E] = {
    JavaConverters.asScalaIteratorConverter(collection.iterator()).asScala.toStream
  }

  implicit def localDateTimeFormat(dateTime: LocalDateTime): String = {
    dateTime format DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
  }

}

@Converter(autoApply = true)
class LocalDateConverter extends AttributeConverter[LocalDateTime, Date] {

  override def convertToDatabaseColumn(date: LocalDateTime): Date = {
    Date.from(date.atZone(ZoneId.systemDefault()).toInstant)
  }

  override def convertToEntityAttribute(date: Date): LocalDateTime = {
    LocalDateTime.from(date.toInstant)
  }

}

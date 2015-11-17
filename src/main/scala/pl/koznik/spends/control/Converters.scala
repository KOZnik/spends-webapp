package pl.koznik.spends.control

import java.time.{LocalDate, LocalDateTime, ZoneId}
import java.util.Date
import java.util.function.BiFunction
import javax.persistence.{AttributeConverter, Converter}

import pl.koznik.spends.entity.Category

import scala.collection.{JavaConversions, JavaConverters}

object Converters {

  implicit def collectionToScalaList[E](collection: java.util.Collection[E]): List[E] = {
    JavaConverters.asScalaIteratorConverter(collection.iterator()).asScala.toList
  }

  implicit def sequenceToJavaList[E](seq: Seq[E]): java.util.List[E] = {
    JavaConversions.seqAsJavaList(seq)
  }

  def funcToJavaBiFunc[K,V](func: (K,V) => V): BiFunction[_ >: K, _ >: V, _ <: V] = {
    new BiFunction[K, V, V] {
      override def apply(key: K, value: V): V = {
        func.apply(key, value)
      }
    }
  }

}

@Converter(autoApply = true)
class LocalDateTimeConverter extends AttributeConverter[LocalDateTime, Date] {

  override def convertToDatabaseColumn(date: LocalDateTime): Date = {
    Date.from(date.atZone(ZoneId.systemDefault()).toInstant)
  }

  override def convertToEntityAttribute(date: Date): LocalDateTime = {
    LocalDateTime.from(date.toInstant)
  }

}

@Converter(autoApply = true)
class LocalDateConverter extends AttributeConverter[LocalDate, Date] {

  override def convertToDatabaseColumn(date: LocalDate): Date = {
    Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant)
  }

  override def convertToEntityAttribute(date: Date): LocalDate = {
    LocalDate.from(date.toInstant)
  }

}

@Converter(autoApply = true)
class CategoryConverter extends AttributeConverter[Category.Category, String] {

  override def convertToDatabaseColumn(cat: Category.Category): String = {
    cat.toString
  }

  override def convertToEntityAttribute(cat: String): Category.Category = {
    Category.forName(cat).get
  }

}

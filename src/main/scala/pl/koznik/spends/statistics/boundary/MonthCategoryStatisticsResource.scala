package pl.koznik.spends.statistics.boundary

import java.time.{LocalDate, ZoneOffset}
import java.util
import javax.inject.Inject
import javax.ws.rs.core.MediaType
import javax.ws.rs.{GET, Path, Produces}

import pl.koznik.spends.common.control.Converters._
import pl.koznik.spends.statistics.control.MonthCategoryStatisticsRepository

@Path("statistics/monthly")
@Produces(Array(MediaType.APPLICATION_JSON))
class MonthCategoryStatisticsResource {

  @Inject
  var repository: MonthCategoryStatisticsRepository = _

  @GET
  def all(): util.Map[Long, util.Map[String, Double]] = {
    val map: util.Map[Long, util.Map[String, Double]] = new util.HashMap[Long, util.Map[String, Double]]()
    repository.all().foreach(stat => {
      val amounts: util.Map[LocalDate, java.lang.Double] = stat.getAmounts
      for ((date: LocalDate) <- amounts.keySet()) {
        val inMilliseconds = date.atStartOfDay(ZoneOffset.ofTotalSeconds(60 * 60)).toInstant.toEpochMilli
        val monthMap = Option.apply(map.get(inMilliseconds)).getOrElse(new util.HashMap[String, Double]())
        monthMap.put(stat.category.toString, monthMap.getOrDefault(stat.category.toString, 0) + amounts.get(date))
        map.put(inMilliseconds, monthMap)
      }
    })
    map
  }

}

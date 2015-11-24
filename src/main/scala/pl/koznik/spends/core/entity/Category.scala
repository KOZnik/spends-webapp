package pl.koznik.spends.core.entity

object Category extends Enumeration {
  type Category = Value
  val FOOD, MY, WIFE, SON, CAR, TAXES, DIFFERENT, UNKNOWN = Value

  def forName(name: String) = if (values.exists(_.toString == name)) Option.apply(withName(name)) else Option.empty

}
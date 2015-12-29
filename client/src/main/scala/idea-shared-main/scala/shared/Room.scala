package shared

import scala.scalajs.js.annotation.JSExportAll

@JSExportAll
case class Room(id: String, name: String, presentation: String, header: String, images: String, isAnApartment: Boolean, price: String)

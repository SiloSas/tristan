package Room

import com.greencatsoft.angularjs.core.Scope

import scala.scalajs.js
import shared.Room

trait RoomScope extends Scope {

  var rooms: js.Array[Room] = js.native

}
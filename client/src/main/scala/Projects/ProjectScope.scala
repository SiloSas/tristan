package Projects

import com.greencatsoft.angularjs.core.Scope

import scala.scalajs.js

trait ProjectScope extends Scope {
  var projects: js.Array[Project] = js.native
  var tags: js.Array[String] = js.native
  var index: Int = js.native
}
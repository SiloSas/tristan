package Shared

import scala.scalajs.js
import scala.scalajs.js.annotation.JSExportAll

@JSExportAll
case class Project(id: String, name: String, description: String, image: String, tags: js.Array[String], technologies: js.Array[String], date: js.Date,
                   isLandscape: Boolean, maxWidth: Int, maxHeight: Int, column: Int)
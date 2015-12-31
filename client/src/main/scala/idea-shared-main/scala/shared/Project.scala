package ClientClass

import scala.scalajs.js
import scala.scalajs.js.annotation.JSExportAll

@JSExportAll
case class Project(id: String, name: String, description: String, image: String, tags: js.Array[String], technologies: js.Array[String], date: String,
                   isLandscape: Boolean, maxWidth: Int, maxHeight: Int, columnNumber: Int)
@JSExportAll
case class ProjectToPost(id: String, name: String, description: String, image: String, tags: Array[String], technologies: Array[String], date: String,
                   isLandscape: Boolean, maxWidth: Int, maxHeight: Int, columnNumber: Int)

@JSExportAll
case class Technology(technologies: String)
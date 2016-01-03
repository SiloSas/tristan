package Admin

import Projects.Project
import com.greencatsoft.angularjs.core.Scope
import org.scalajs.dom.raw.{File, ImageData}

import scala.scalajs.js

trait AdminScope extends Scope {
  var projects: js.Array[MutableProject] = js.native
  var tags: js.Array[String] = js.native
  var technologies: js.Array[String] = js.native
  var newProject: MutableProject = js.native
  var newImage: File = js.native
  var status: String = js.native
}

trait GalleryColumn extends js.Object {
  var number: Int = js.native
  var size: Int = js.native
}

trait MutableProject extends js.Object {
  var id: String = js.native
  var name: String = js.native
  var description: String = js.native
  var image: String = js.native
  var tags: js.Array[String] = js.native
  var technologies: js.Array[String] = js.native
  var date: js.Date = js.native
  var isLandscape: Boolean= js.native
  var maxWidth: Int= js.native
  var maxHeight: Int= js.native
  var columnNumber: Int= js.native
}
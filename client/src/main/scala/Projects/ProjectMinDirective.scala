package Projects

import com.greencatsoft.angularjs.extensions.{ModalOptions, ModalService}
import com.greencatsoft.angularjs.{Attributes, TemplatedDirective, ElementDirective, injectable}
import org.scalajs.dom.Element
import org.scalajs.dom._
import org.scalajs.dom.html._
import org.scalajs.dom.raw.MouseEvent

import scala.scalajs.js
import scala.scalajs.js.annotation.JSExport

@JSExport
@injectable("projectMin")
class ProjectMinDirective() extends ElementDirective with TemplatedDirective {
  override val templateUrl = "assets/templates/Projects/projectMin.html"
}


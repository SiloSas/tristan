package Room

import com.greencatsoft.angularjs.extensions.{ModalOptions, ModalService}
import com.greencatsoft.angularjs.{Attributes, TemplatedDirective, ElementDirective, injectable}
import org.scalajs.dom.Element
import org.scalajs.dom._
import org.scalajs.dom.html._
import org.scalajs.dom.raw.MouseEvent

import scala.scalajs.js
import scala.scalajs.js.annotation.JSExport

@JSExport
@injectable("roomMin")
class RoomMinDirective(modal: ModalService) extends ElementDirective with TemplatedDirective {
  override val templateUrl = "assets/templates/Room/roomMin.html"
}


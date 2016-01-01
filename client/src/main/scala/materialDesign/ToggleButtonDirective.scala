package materialDesign

import Room.RoomScope
import com.greencatsoft.angularjs.extensions.ModalService
import com.greencatsoft.angularjs.extensions.material.Sidenav
import com.greencatsoft.angularjs._
import example.RoomService
import org.scalajs.dom.Element
import org.scalajs.dom.html.Html

import scala.scalajs.js.annotation.JSExportAll


@JSExportAll
@injectable("toggleButton")
class ToggleButtonDirective(sidenav: Sidenav) extends ClassDirective {

  def toggleLeft: Unit = {
    sidenav("left").toggle()
  }
  def close: Unit = {
    sidenav("left").close()
  }
}
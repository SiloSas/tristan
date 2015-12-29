import Projects.{ProjectMinDirective, SliderDirective, ProjectController}
import Room.{RoomController, RoomMinDirective}
import com.greencatsoft.angularjs._
import example.RoomServiceFactory

import scala.scalajs.js.JSApp
import scala.scalajs.js.annotation.JSExport

@JSExport
object App extends JSApp {

  override def main() {
    val module = Angular.module("app", Seq("ngAnimate", "ngAria", "ngMaterial", "mm.foundation", "ngRoute", "ngMap"))

    module
    .factory[RoomServiceFactory]
    .controller[RoomController]
    .controller[ProjectController]
    .directive[RoomMinDirective]
    .directive[ProjectMinDirective]
    .directive[SliderDirective]
    .config(RoutingConfig)
  }
}
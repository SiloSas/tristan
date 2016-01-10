import Admin.{ImageProjectDirective, FileReaderDirective, AdminController}
import Projects.{ProjectServiceFactory, ProjectMinDirective, SliderDirective, ProjectController}
import Room.{RoomController, RoomMinDirective}
import com.greencatsoft.angularjs._
import example.RoomServiceFactory
import materialDesign.ToggleButtonDirective
import scala.scalajs.js.JSApp
import scala.scalajs.js.annotation.JSExport

@JSExport
object App extends JSApp {

  override def main() {
    val module = Angular.module("app", Seq("ngAnimate", "ngAria", "ngMaterial", "mm.foundation", "ngRoute", "ngMap", "uploader"))

    module
    .factory[RoomServiceFactory]
    .factory[ProjectServiceFactory]
    .controller[RoomController]
    .controller[ProjectController]
    .controller[AdminController]
    .directive[RoomMinDirective]
    .directive[FileReaderDirective]
    .directive[ProjectMinDirective]
    .directive[SliderDirective]
    .directive[ToggleButtonDirective]
    .directive[ImageProjectDirective]
    .config(RoutingConfig)
  }
}
package Room


import com.greencatsoft.angularjs.core.{RouteParams, Timeout}
import com.greencatsoft.angularjs.{AbstractController, injectable}
import example.RoomService
import scala.scalajs.js
import org.scalajs.dom
import shared.{Room, SharedMessages}
import scala.concurrent.ExecutionContext.Implicits.global
import org.scalajs.dom.console
import scala.scalajs.js.annotation.{JSExportAll, JSExport}
import scala.scalajs.js.JSConverters.JSRichGenTraversableOnce
import scala.util.{Failure, Success}


@JSExportAll
@injectable("roomController")
class RoomController(scope: RoomScope, service: RoomService) extends AbstractController[RoomScope](scope) {


  scope.rooms = js.Array[Room]()
  service.findAll() onComplete {
    case Success(rooms) =>
      scope.$apply {
        scope.rooms = rooms.toJSArray
      }
    case Failure(t) => handleError(t)
  }

  private def handleError(t: Throwable) {
    console.error(s"An error has occured: $t")
  }

}

package Room

import javax.inject.Inject

import play.api.db.slick.DatabaseConfigProvider
import play.api.mvc.{Action, _}
import shared.Room
import upickle.default._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class RoomController
    extends Controller {
  def findAll() = Action.async {
    Future(Seq.empty[Room]).map { rooms =>
      Ok(write(rooms))
    }
  }
}
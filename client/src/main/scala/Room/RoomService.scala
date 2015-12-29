package example

import com.greencatsoft.angularjs.core.HttpService
import com.greencatsoft.angularjs.{Factory, Service, injectable}
import upickle.default._
import org.scalajs.dom
import scala.scalajs.js
import scala.scalajs.js.JSON
import scala.scalajs.js.annotation.JSExport
import scala.concurrent.Future
import scala.scalajs.concurrent.JSExecutionContext.Implicits.runNow
import scala.util.{Failure, Success, Try}
import shared._

@injectable("roomService")
class RoomService(http: HttpService) extends Service {
  require(http != null, "Missing argument 'http'.")


  @JSExport
  def findAll(): Future[Seq[Room]] = /*flatten*/ {
    // Append a timestamp to prevent some old browsers from caching the result.
    http.get[js.Any]("/rooms")
      .map {JSON.stringify(_)}
      .map { read[Seq[Room]] }
  }

  protected def flatten[T](future: Future[Try[T]]): Future[T] = future flatMap {
    case Success(s) => Future.successful(s)
    case Failure(f) => Future.failed(f)
  }
  protected def parameterizeUrl(url: String, parameters: Map[String, Any]): String = {
    require(url != null, "Missing argument 'url'.")
    require(parameters != null, "Missing argument 'parameters'.")

    parameters.foldLeft(url)((base, kv) =>
      base ++ { if (base.contains("?")) "&" else "?" } ++ kv._1 ++ "=" + kv._2)
  }
}


@injectable("roomService")
class RoomServiceFactory(http: HttpService) extends Factory[RoomService] {

  override def apply(): RoomService = new RoomService(http)
}

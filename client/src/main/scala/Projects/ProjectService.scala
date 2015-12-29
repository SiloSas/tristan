package Projects

import ClientClass.Project
import com.greencatsoft.angularjs.core.HttpService
import com.greencatsoft.angularjs.{Factory, Service, injectable}
import upickle.default._
import org.scalajs.dom.console
import scala.scalajs.js
import scala.scalajs.js.JSON
import scala.scalajs.js.annotation.JSExport
import scala.concurrent.Future
import scala.scalajs.concurrent.JSExecutionContext.Implicits.runNow
import scala.util.{Failure, Success, Try}
import shared._

@injectable("projectService")
class ProjectService(http: HttpService) extends Service {
  require(http != null, "Missing argument 'http'.")


  @JSExport
  def findAll(): Future[Seq[Project]] = /*flatten*/ {
    // Append a timestamp to prevent some old browsers from caching the result.
    http.get[js.Any]("/projects")
      .map { p =>
        console.log(p)
        JSON.stringify(p)
      }
      .map { read[Seq[Project]] }
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


@injectable("projectService")
class ProjectServiceFactory(http: HttpService) extends Factory[ProjectService] {

  override def apply(): ProjectService = new ProjectService(http)
}

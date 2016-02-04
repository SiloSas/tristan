package Projects

import java.util.UUID

import Admin.MutableProject
import com.greencatsoft.angularjs._
import com.greencatsoft.angularjs.core.{SceService, HttpConfig, HttpService}
import org.scalajs.dom.console
import org.scalajs.dom.raw.{File, FormData}
import upickle.default._

import scala.concurrent.Future
import scala.scalajs.concurrent.JSExecutionContext.Implicits.runNow
import scala.scalajs.js
import scala.scalajs.js.annotation.JSExport
import scala.scalajs.js.{JSON, Object}
import scala.util.{Failure, Success, Try}
import org.scalajs.dom.window

case class Height(height: Double)

@injectable("projectService")
class ProjectService(http: HttpService) extends Service {
  require(http != null, "Missing argument 'http'.")


  @JSExport
  def getContact(): Future[js.Any] = /*flatten*/ {
    // Append a timestamp to prevent some old browsers from caching the result.
    http.get[js.Any]("/contact")
      .map { p =>
        console.log(p)
        JSON.stringify(p)
        p.toString.replaceFirst("\"", "").dropRight(1)
      }
  }
  @JSExport
  def updateContact(contact: String): Future[String] = /*flatten*/ {
    // Append a timestamp to prevent some old browsers from caching the result.
    http.put[js.Any]("/contact?contact=" + contact)
      .map { p =>
        console.log(p)
        p.toString
      }
  }

  @JSExport
  def findAll(): Future[Seq[Project]] = /*flatten*/ {
    // Append a timestamp to prevent some old browsers from caching the result.
    http.get[js.Any]("/projects")
      .map { p =>
        console.log(p)
        JSON.stringify(p)
      }
      .map { string =>
        val projects = read[Seq[Project]](string)
        projects map { project =>
          val ratio: Double = js.eval("window.devicePixelRatio").toString.toDouble
          val checkedRatio = if (ratio > 0) ratio else 1.0
          val maxWidth = if (project.maxWidth > window.innerWidth*checkedRatio) window.innerWidth*checkedRatio else project.maxWidth
          val indexToTake = if (project.image.indexOf("?maxWidth=") > -1) project.image.indexOf("?maxWidth=") - 10 else project.image.length
          project.copy(image = project.image.take(indexToTake) + "?maxWidth=" + maxWidth.toInt)
        }
      }
  }

  @JSExport
  def update(project: MutableProject): Future[String] = /*flatten*/ {
    val projectToPost: ProjectToPost = ProjectToPost(id = project.id, name = project.name, description = project.description,
      image = project.image, tags = project.tags.toArray, technologies = project.technologies.toArray,
      date = project.date.getFullYear().toString + "-" + project.date.getMonth().toString + "-" + project.date.getDay().toString,
      isLandscape = project.isLandscape, maxWidth = project.maxWidth, maxHeight = project.maxHeight, columnNumber = project.columnNumber)
    http.put[js.Any](s"/projects/${project.id}", write(projectToPost))
      .map { p =>
        console.log(p)
        JSON.stringify(p)
      }
  }

  @JSExport
  def add(project: MutableProject): Future[String] = /*flatten*/ {
    val projectToPost: ProjectToPost = ProjectToPost(id = UUID.randomUUID().toString, name = project.name, description = project.description,
      image = project.image, tags = project.tags.toArray, technologies = project.technologies.toArray,
      date = project.date.getFullYear().toString + "-" + project.date.getMonth().toString + "-" + project.date.getDay().toString,
      isLandscape = project.isLandscape, maxWidth = project.maxWidth, maxHeight = project.maxHeight, columnNumber = project.columnNumber)
    http.post[js.Any](s"/projects", write(projectToPost))
      .map { p =>
        console.log(p)
        JSON.stringify(p)
      }
  }

  @JSExport
  def delete(id: String): Future[String] = {
    http.delete[js.Any](s"/projects/$id")
    .map { resp =>
      JSON.stringify(resp)
    }
  }

  @JSExport
  def findTechnologies(): Future[Seq[Technology]] = {
    http.get[js.Any]("/technologies")
    .map { resp =>
      console.log(resp)
      JSON.stringify(resp)
    } map {
      read[Seq[Technology]]
    }
  }

  @JSExport
  def findBaseHeight(): Future[Seq[Height]] = {
    http.get[js.Any]("/baseHeight")
    .map { resp =>
      console.log(resp)
      JSON.stringify(resp)
    } map {
      read[Seq[Height]]
    }
  }
  @JSExport
  def updateBaseHeight(newHeight: Double): Future[String] = {
    http.put[js.Any]("/baseHeight/" + newHeight)
    .map { resp =>
      console.log(resp)
      JSON.stringify(resp)
    }
  }

  @JSExport
  def postImage(image: File): Future[String] = {
    val fd = new FormData()
    fd.append("picture", image)
    val configs = new Object().asInstanceOf[HttpConfig]
    configs.headers = js.Dictionary("method" -> "POST","Content-Type" -> "multipart/form-data")
    http.post[js.Any]("/upload", fd, configs)
    .map { resp =>
      JSON.stringify(resp)
    }
  }
  /*
  * var fd = new FormData();
            fd.append('picture', image.file);
            $http.post('/upload', fd, {
                transformRequest: angular.identity,
                headers: {'Content-Type': undefined}*/
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

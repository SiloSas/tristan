package Projects

import java.util.{Dictionary, UUID}

import Admin.MutableProject
import ClientClass.{Technology, ProjectToPost, Project}
import com.greencatsoft.angularjs.core.{HttpConfig, HttpService}
import com.greencatsoft.angularjs._
import com.sun.xml.internal.ws.server.sei.EndpointArgumentsBuilder.Header
import org.scalajs.dom.raw.{File, FormData, ImageData}
import upickle.default._
import org.scalajs.dom.console
import scala.scalajs.js
import scala.scalajs.js.{Dictionary, Object, JSON}
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

  @JSExport
  def update(project: MutableProject): Future[String] = /*flatten*/ {
    val projectToPost: ProjectToPost = ProjectToPost(id = project.id, name = project.name, description = project.description,
      image = project.image, tags = project.tags.toArray, technologies = project.technologies.toArray,
      date = project.date.getFullYear().toString + "-" + project.date.getMonth().toString + "-" + project.date.getDay().toString,
      isLandscape = project.isLandscape, maxWidth = project.maxWidth, maxHeight = project.maxHeight, columnNumber = project.column)
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
      isLandscape = project.isLandscape, maxWidth = project.maxWidth, maxHeight = project.maxHeight, columnNumber = project.column)
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

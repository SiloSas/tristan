package Projects


import java.io.File
import javax.inject.Inject

import administration.Authenticated
import play.api.db.slick.DatabaseConfigProvider
import play.api.libs.json.JsObject
import play.api.mvc.{Action, _}
import upickle.default._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success}

class ProjectsController @Inject()(protected val dbConfigProvider: DatabaseConfigProvider, val projectMethods: ProjectMethods)
  extends Controller {

  def findAll() = Action.async {
    projectMethods.findAll.map { projects =>
      Ok(write(projects))
    }
  }

  def findTechnologies() = Action.async {
    projectMethods.findTechnologies.map { technologies =>
      println(technologies)
      Ok(write(technologies))
    }
  }

  def update(id: String) = process(projectMethods.update)

  def add() = process(projectMethods.add)
  
  def delete(id: String) = Authenticated.async {
    projectMethods.delete(id).map { result =>
      Ok(write(result))
    }
  }

  def uploadImage = Authenticated(parse.multipartFormData) { request =>
    request.body.file("picture").map { image =>
      image.contentType match {
        case Some(fileExtension)  =>

          println(image)
          val filename = image.filename
          image.ref.moveTo(new File("server/public/images/" + filename), replace = true)

          Ok("assets/images/" +filename)

        case _ =>
          Unauthorized("Wrong content type")
      }
    }.getOrElse { BadRequest }
  }

  def process(updater: Shared.Project => Future[Int]) = Authenticated.async(parse.json) { request =>
    val data = request.body.as[JsObject]

    val a = updater(read[Shared.Project](data.toString()))
    a.map { result =>
      Ok(write(result))
    }
  }
}
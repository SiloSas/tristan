package Projects


import javax.inject.Inject

import play.api.db.slick.DatabaseConfigProvider
import play.api.mvc.{Action, _}
import upickle.default._

import scala.concurrent.ExecutionContext.Implicits.global

class ProjectsController @Inject()(protected val dbConfigProvider: DatabaseConfigProvider, val projectMethods: ProjectMethods)
  extends Controller {
  def findAll() = Action.async {
    projectMethods.findAll.map { projects =>
      Ok(write(projects))
    }
  }
}
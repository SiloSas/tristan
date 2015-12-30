package Projects

import javax.inject.Inject
import Shared.Project
import database.MyPostgresDriver.api._
import database.{MyDBTableDefinitions, MyPostgresDriver}
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import play.api.libs.concurrent.Execution.Implicits._
import scala.concurrent.Future
import scala.language.postfixOps


class ProjectMethods @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)
  extends HasDatabaseConfigProvider[MyPostgresDriver]
  with MyDBTableDefinitions {

  def findAll: Future[Seq[Project]] = db.run(projects.sortBy(_.date.desc).result) map (_.toSeq)

  def update(project: Project): Future[Int] = db.run(projects.filter(_.id === project.id).update(project))

  def add(project: Project): Future[Int] = db.run(projects += project)

  def delete(id: String): Future[Int] = db.run(projects.filter(_.id === id).delete)
}
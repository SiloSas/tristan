package Projects

import javax.inject.Inject
import Shared.{Technology, Project}
import database.MyPostgresDriver.api._
import database.{Contact, MyDBTableDefinitions, MyPostgresDriver, Height}
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import play.api.libs.concurrent.Execution.Implicits._
import scala.concurrent.Future
import scala.language.postfixOps


class ProjectMethods @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)
  extends HasDatabaseConfigProvider[MyPostgresDriver]
  with MyDBTableDefinitions {

  def findAll: Future[Seq[Project]] = db.run(projects.sortBy(_.date.desc).result) map (_.toSeq)

  def update(project: Project): Future[Int] = db.run(projects.filter(_.id === project.id).update(project))

  def addTechnology(technology: Technology): Future[Int] = db.run(technologies += technology)

  def add(project: Project): Future[Int] = {
    val technologies = project.technologies
    technologies.map { technology =>
      addTechnology(Technology(technology))
    }
    db.run {
      projects += project
    }
  }
  
  def findTechnologies: Future[Seq[Technology]] = db.run(technologies.result) map (_.toSeq)

  def findContact: Future[String] = db.run(contacts.result) map (_.head.contact)
  def updateContact(contact: Contact): Future[Int] = db.run(contacts.update(contact))

  def delete(id: String): Future[Int] = db.run(projects.filter(_.id === id).delete)

  def findBaseHeight: Future[Seq[Height]] = db.run(baseheight.result) map (_.toSeq)
  def updateBaseHeight(height: Height): Future[Int] = db.run(baseheight.update(height))
}
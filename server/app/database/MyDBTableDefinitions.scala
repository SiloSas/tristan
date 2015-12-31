package database

import java.sql.Date
import java.text.{DateFormat, SimpleDateFormat}

import Shared.{Technology, Project}
import administration.UserActor.User
import shared.Room
import MyPostgresDriver.api._

trait MyDBTableDefinitions {

  def optionStringToSet(maybeString: Option[String]): Array[String] = maybeString match {
    case None => Array.empty
    case Some(string) => string.split(",").map(_.trim).filter(_.nonEmpty).toArray
  }

  class Rooms(tag: Tag) extends Table[Room](tag, "rooms") {
    def id = column[String]("id")
    def name = column[String]("name")
    def presentation = column[String]("presentation")
    def header = column[String]("header")
    def images = column[String]("images")
    def isAnApartment = column[Boolean]("isanapartment")
    def price = column[String]("price")

    def * = (id, name, presentation, header, images, isAnApartment, price) <> ((Room.apply _).tupled, Room.unapply)
  }
  lazy val rooms = TableQuery[Rooms]

  val inputDateFormat: DateFormat = new SimpleDateFormat("yyyy-MM-dd")

  class Projects(tag: Tag) extends Table[Project](tag, "projects") {
    def id = column[String]("id")
    def name = column[String]("name")
    def description = column[String]("description")
    def image = column[String]("image")
    def tags = column[Option[String]]("tags")
    def technologies = column[Option[String]]("technologies")
    def date = column[Date]("date")
    def isLandscape = column[Boolean]("islandscape")
    def maxWidth = column[Int]("maxwidth")
    def maxHeight = column[Int]("maxheight")
    def columnNumber = column[Int]("columnnumber")

    def * = (id, name, description, image, tags, technologies, date, isLandscape, maxWidth, maxHeight, columnNumber).shaped <> (
      { case (id, name, description, image, tags, technologies, date, isLandscape, maxWidth, maxHeight, columnNumber) =>
        Project(id, name, description, image, optionStringToSet(tags), optionStringToSet(technologies), date.toString,
          isLandscape, maxWidth, maxHeight, columnNumber)
    }, { project: Project =>
      Some((project.id, project.name, project.description, project.image, Option(project.tags.mkString(",")),
        Option(project.technologies.mkString(",")), new java.sql.Date(inputDateFormat.parse(project.date).getTime),
        project.isLandscape, project.maxWidth, project.maxHeight, project.columnNumber))
    })

  }

  lazy val projects = TableQuery[Projects]

  class Technologies(tag: Tag) extends Table[Technology](tag, "technologies") {
    def technologies = column[String]("technologies")

    def * = technologies <> (Technology.apply, Technology.unapply)
  }
  lazy val technologies = TableQuery[Technologies]

  class Users(tag: Tag) extends Table[User](tag, "users") {
    def id = column[Int]("userid", O.PrimaryKey)
    def login = column[String]("login")
    def password = column[String]("password")

    def * = login <> (User, User.unapply)
  }
  lazy val users = TableQuery[Users]

}
package administration

import javax.inject.Inject

import akka.actor._
import database.{MyDBTableDefinitions, MyPostgresDriver}
import org.mindrot.jbcrypt.BCrypt
import play.api.Logger
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import play.api.libs.concurrent.Execution.Implicits._
import play.api.mvc._
import database.MyPostgresDriver.api._

import scala.concurrent.Future
import scala.language.{implicitConversions, postfixOps}


class AuthenticatedRequest[A](request: Request[A]) extends WrappedRequest[A](request)

object Authenticated extends ActionBuilder[AuthenticatedRequest] {
  def invokeBlock[A](request: Request[A], block: (AuthenticatedRequest[A]) => Future[Result]) = {
    request.session.get("connected") match {
      case Some(uuid) =>
        block(new AuthenticatedRequest(request))
      case None =>
        //        block(new AuthenticatedRequest(request = Request[Result](UNAUTHORIZED)))
        Future.successful(Results.Unauthorized("DTC"))
    }
  }
}

object UserActor {
  def props = Props[UserActor]
  case class User(login: String)
  case class AuthenticationRequest[A](login: String, password: String)

  case class AuthenticationResponse(authorized: Boolean)
}

class UserActor @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)
  extends Actor
  with HasDatabaseConfigProvider[MyPostgresDriver]
  with MyDBTableDefinitions {

  import UserActor._

  def receive = {
    case AuthenticationRequest(login: String, password: String) =>
      val authenticationResponse = verifyIdentity(login, password)
      sender ! authenticationResponse

    case _ =>
      Logger error "UserActor.receive: unknown request"
  }


  def verifyIdentity(login: String, password: String): Future[AuthenticationResponse] = {
    db.run(users
      .filter(_.login === login)
      .map(user => user.password)
      .result
      .headOption) map {
      case None =>
        AuthenticationResponse(false)
      case Some(passwordFound) =>
        //          val salt = BCrypt.gensalt(7)
        //          println(BCrypt.hashpw("admin", salt))
        AuthenticationResponse(BCrypt.checkpw(password, passwordFound))
    }
  }
}
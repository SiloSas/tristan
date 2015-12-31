
package administration

import javax.inject.{Inject, Named, Singleton}

import administration.UserActor.{AuthenticationRequest, AuthenticationResponse}
import akka.actor.ActorRef
import akka.pattern.ask
import akka.util.Timeout
import controllers.Application._
import play.api.db.slick.DatabaseConfigProvider
import play.api.mvc.{Controller, _}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.concurrent.duration._
import scala.language.{implicitConversions, postfixOps}


@Singleton
class AuthenticationController @Inject()(protected val dbConfigProvider: DatabaseConfigProvider,
                                         @Named("user-actor") userActor: ActorRef)
  extends Controller {

  implicit val timeout = Timeout(5 seconds)

  def authenticate(login: String, password: String) = Action.async {
    (userActor ? AuthenticationRequest(login, password)).mapTo[Future[AuthenticationResponse]] flatMap { resp =>
      resp map {
        case AuthenticationResponse(true) => Ok(views.html.admin("")).withSession("connected" -> "true")
        case AuthenticationResponse(false) => Ok(views.html.connection("Identifiants incorrect"))
      }
    }
  }

  def logout = Action { Ok("Correctly logged out").withNewSession }

  //  def sendNotificationMail(content: String, brandUUID: UUID, isClient: Boolean): Unit = {
  //    val mail = use[MailerPlugin].email
  //    mail.setSubject("BO DEA notification")
  //    mail.addFrom("ticketappfrance@gmail.com")
  //    play.api.db.slick.DB.withSession { implicit session =>
  //      isClient match {
  //        case true =>
  //          val loginList = users.filter(_.role === 1).map(_.login).list
  //          Logger info "mails will be send to " + loginList
  //          loginList.foreach { login =>
  //            mail.addRecipient(login)
  //            mail.send(content)
  //          }
  //
  //        case false =>
  //          val userLoginList = userBrand
  //            .filter(_.brandId === brandUUID) rightJoin
  //            users.map(_.login)
  //
  //          userLoginList.map { login =>
  //            val loginList = users.filter(_.role === 2).map(_.login).list
  //            Logger info "mails will be send to " + loginList
  //            loginList.foreach { login =>
  //              mail.addRecipient(login)
  //              mail.send(content)
  //            }
  //          }
  //      }
  //    }
  //  }

  //  def uploadImage = Authenticated(parse.multipartFormData) { request =>
  //    request.uuid match {
  //      case None =>
  //        Unauthorized("Unauthorized")
  //      case _ =>
  //        request.body.file("picture").map { image =>
  //
  //          image.contentType match {
  //            case Some(fileExtension) if fileExtension == "image/tiff" || fileExtension == "image/jpg" ||
  //              fileExtension == "image/jpeg" || fileExtension == "image/png" || fileExtension == "image/svg" ||
  //              fileExtension == "application/pdf" =>
  //
  //              val filename = UUID.randomUUID().toString + image.filename
  //              image.ref.moveTo(new File("public/pictures/" + filename), replace = true)
  //
  //              Ok(filename)
  //
  //            case _ =>
  //              Unauthorized("Wrong content type")
  //          }
  //        }.getOrElse { BadRequest }
  //    }
  //  }
}
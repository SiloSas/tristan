package Projects


import java.io.{ByteArrayOutputStream, File}
import javax.imageio.ImageIO
import javax.inject.Inject

import administration.Authenticated
import com.sksamuel.scrimage.Image
import com.sksamuel.scrimage.ScaleMethod.Bicubic
import database.{Contact, Height}
import play.Play
import play.api.db.slick.DatabaseConfigProvider
import play.api.libs.json.JsObject
import play.api.mvc._
import upickle.default._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class ProjectsController @Inject()(protected val dbConfigProvider: DatabaseConfigProvider, val projectMethods: ProjectMethods)
  extends Controller {

  def findAll() = Action.async {
    projectMethods.findAll.map { projects =>
      Ok(write(projects))
    }
  }

  def findBaseHeight() = Action.async {
    projectMethods.findBaseHeight.map { baseHeight =>
      Ok(write(baseHeight))
    }
  }
  def updateBaseHeight(newHeight: Double) = Authenticated.async {
    projectMethods.updateBaseHeight(Height(newHeight)).map { baseHeight =>
      Ok(write(baseHeight))
    }
  }
  def findTechnologies() = Action.async {
    projectMethods.findTechnologies.map { technologies =>
      println(technologies)
      Ok(write(technologies))
    }
  }
  def findContact() = Action.async {
    projectMethods.findContact.map { contact =>
      Ok(write(contact))
    }
  }

  def updateContact(contact: String) = Authenticated.async {
    projectMethods.updateContact(Contact(contact)).map { resp =>
      Ok(write(resp))
    }
  }

  def update(id: String) = process(projectMethods.update)

  def add() = process(projectMethods.add)

  def delete(id: String) = Authenticated.async {
    projectMethods.delete(id).map { result =>
      Ok(write(result))
    }
  }

  def getImage(fileName: String, maxWidth: Int) = Action {
    val imageFile = new File(Play.application().path().getPath + "/public/images/" + fileName)
    if (imageFile.length > 0) {
      val in = Image.fromFile(imageFile)
      val scale = if (in.width > maxWidth) maxWidth.toDouble/in.width.toDouble else 1.0
      val out = in.scale(scale, Bicubic).bytes
      val image = ImageIO.read(imageFile)
      val resourceType = fileName.substring(fileName.length()-3)
      println(imageFile + resourceType)
      val baos = new ByteArrayOutputStream()
      ImageIO.write(image, resourceType, baos)

      Ok(out).as("image/" + resourceType)
      //resource type such as image+png, image+jpg
    } else {
      NotFound(fileName)
    }
  }

  def uploadImage = Authenticated(parse.multipartFormData) { request =>
    request.body.file("picture").map { image =>
      image.contentType match {
        case Some(fileExtension)  =>

          println(image)
          val filename = image.filename
          image.ref.moveTo(new File(Play.application().path().getPath + "/public/images/" + filename), replace = true)

          Ok("images/" +filename)

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
package Admin

import Projects.{Project, ProjectService}
import com.greencatsoft.angularjs.core.Timeout
import com.greencatsoft.angularjs.{AbstractController, injectable}
import org.scalajs.dom._
import org.scalajs.dom.html.Image

import scala.concurrent.ExecutionContext.Implicits.global
import scala.scalajs.js
import scala.scalajs.js.JSConverters.JSRichGenTraversableOnce
import scala.scalajs.js.annotation.JSExportAll
import scala.scalajs.js.{Date, Object}
import scala.util.{Failure, Success}

@JSExportAll
@injectable("adminController")
class AdminController(adminScope: AdminScope, timeout: Timeout, projectService: ProjectService) extends AbstractController[AdminScope](adminScope) {

  var isOpen = false
  var projects: Seq[Project] = Seq.empty
  adminScope.tags = Seq.empty[String].toJSArray
  adminScope.technologies = Seq.empty[String].toJSArray
  var maxWidth = 0.0
  var maxHeight = 0.0
  var limit=10
  var baseHeight = 300.0
  projectService.findBaseHeight().onComplete {
    case Success(heightSeq) =>
      baseHeight = heightSeq.headOption match {
        case Some(height) =>
          height.height
        case _ =>
          300.0
      }
      getProjects
    case _ =>
      getProjects
      console.log("error get height")
  }

  def getProjects: Unit = {
    projectService.findAll().onComplete  {
      case Success(projectsFound) =>
        timeout( () => {
          projects = projectsFound
          adminScope.projects = projects.map { project =>
            val adminProject = new Object().asInstanceOf[MutableProject]
            adminProject.id = project.id
            adminProject.name = project.name
            adminProject.date = new Date(project.date)
            adminProject.description = project.description
            adminProject.image = project.image
            adminProject.isLandscape = project.isLandscape
            adminProject.maxHeight = project.maxHeight
            adminProject.maxWidth = project.maxWidth
            adminProject.tags = project.tags
            adminProject.technologies = project.technologies
            adminProject.columnNumber = if (project.columnNumber == 0) {
              projects.indexOf(project)
            } else project.columnNumber
            adminProject
          }.toJSArray
          adminScope.projects.map { project =>
            project.tags foreach { tag =>
              if (adminScope.tags.indexOf(tag) == -1)  adminScope.tags = adminScope.tags :+ tag
            }
            project.technologies foreach { technologie =>
              if (adminScope.technologies.indexOf(technologie) == -1)  adminScope.technologies = adminScope.technologies :+ technologie
            }
            project
          }
        }, 0, true)
      case Failure(t: Throwable) =>
        console.log(throw t)
    }
  }


  projectService.findTechnologies().onComplete {
    case Success(technologies) =>
      console.log(technologies)
      timeout( () => {
        technologies.map { technology =>
          if (adminScope.technologies.indexOf(technology.technologies) == -1) adminScope.technologies.push(technology.technologies)
        }
      }, 0, true)
    case Failure(t: Throwable) =>
      console.log(throw t)
  }
  def setProject(project: MutableProject): Unit = {
    timeout( () => {
      adminScope.status = "false"
      adminScope.newProject = project
      maxHeight = project.maxHeight
      maxWidth = project.maxWidth
    })
  }


  def setSize(): Unit = {
    document.getElementById("projectImage").asInstanceOf[Image].addEventListener("load", (event: Event) => {
      timeout( () => {
        val width = document.getElementById("projectImage").asInstanceOf[Image].naturalWidth
        val height = document.getElementById("projectImage").asInstanceOf[Image].naturalHeight
        adminScope.newProject.maxWidth = width
        adminScope.newProject.maxHeight = height
        if (width > height) adminScope.newProject.isLandscape = true
        else adminScope.newProject.isLandscape = false
        maxHeight = height
        maxWidth = width
        console.log(document.getElementById("projectImage").asInstanceOf[Image].naturalWidth)
        console.log(document.getElementById("projectImage").asInstanceOf[Image].naturalHeight)
      }, 10)
    })
  }

  def setNewProject(): Unit = {
    timeout( () => {
      adminScope.status = "false"
      val project = new Object().asInstanceOf[MutableProject]
      project.id = "new"
      project.columnNumber = 0
      project.date = new Date()
      project.tags = Seq.empty[String].toJSArray
      project.technologies = Seq.empty[String].toJSArray
      adminScope.newProject = project
      maxHeight = project.maxHeight
      maxWidth = project.maxWidth
    })
  }
  def addTechnologie(technologie: String): Unit = {
    timeout( () => {
      if(adminScope.newProject.technologies.indexOf(technologie) == -1) adminScope.newProject.technologies.push(technologie)
      if(adminScope.technologies.indexOf(technologie) == -1) adminScope.technologies.push(technologie)
    },0, true)
  }

  def updateProjects(): Unit = {
    timeout( () => {
      adminScope.status = "inProgress"
      if (adminScope.newProject.id != "new")  {

        projectService.update(adminScope.newProject) onComplete {
          case Success(uploaded) =>
            timeout( () => {
              adminScope.newProject.tags foreach { tag =>
                if (adminScope.tags.indexOf(tag) == -1)  adminScope.tags = adminScope.tags :+ tag
              }
              adminScope.status = "validate"
            },0, true)
          case Failure(t: Throwable) =>
            timeout( () => {
              adminScope.status = "error"
            },0, true)
        }
      }
      else projectService.add(adminScope.newProject)onComplete {
        case Success(uploaded) =>
          timeout( () => {
            adminScope.newProject.tags foreach { tag =>
              if (adminScope.tags.indexOf(tag) == -1)  adminScope.tags = adminScope.tags :+ tag
            }
            adminScope.status = "validate"
          },0, true)
        case Failure(t: Throwable) =>
          timeout( () => {
            adminScope.status = "error"
          },0, true)
      }
    },0 ,true)
  }


  def updateAllProjects(): Unit = {
    projectService.updateBaseHeight(baseHeight).onComplete {
      case Success(int) =>
        adminScope.projects.foreach { project =>
          projectService.update(project) onComplete {
            case Success(uploaded) =>
              timeout(() => {
                adminScope.status = "validate"
              }, 0, true)
            case Failure(t: Throwable) =>
              timeout(() => {
                adminScope.status = "error"
              }, 0, true)
          }
        }
      case Failure(t: Throwable) =>
        timeout( () => {
          adminScope.status = "error"
        },0, true)
    }
  }

  def delete(id: String): Unit = {
      projectService.delete(id) onComplete {
        case Success(resp) =>
          timeout( () => {
            adminScope.projects.splice(adminScope.projects.indexOf(adminScope.projects.filter(_.id == id).head), 1)
          }, 0, true)
        case Failure(t: Throwable) =>
          console.log(throw t)
      }
  }

  def updateContact(contact: String): Unit = {
      projectService.updateContact(contact) onComplete {
        case Success(resp) =>
          timeout( () => {
            adminScope.status = "validate"
          }, 0, true)
        case Failure(t: Throwable) =>
          adminScope.status = "error"
          console.log(throw t)
      }
  }

  var contact: js.Any = Nil
  var changeContact = false
  def getContact() = {
    projectService.getContact() onComplete {
      case Success(foundContact) =>
        timeout( () => contact = foundContact)
    }
  }

  def addNewImage(): Unit = {
    console.log(adminScope.newImage)
    projectService.postImage(adminScope.newImage) onComplete {
      case Success(image) =>
        adminScope.newProject.image = image
    }
  }

  def refactorTech(technology: String): String = {
    val a = technology.substring(technology.lastIndexOf("/") + 1).replace(technology.substring(technology.length()-4), "")
    a
  }
  
  def changeSize(isWidth: Boolean, isLocked: Boolean): Unit = {
    if (isLocked) {
      if (isWidth) {
        console.log(maxHeight)
        adminScope.newProject.maxHeight = (adminScope.newProject.maxWidth * (maxHeight/maxWidth)).toInt
      } else adminScope.newProject.maxWidth = (adminScope.newProject.maxHeight * (maxWidth/maxHeight)).toInt
    }
  }


}


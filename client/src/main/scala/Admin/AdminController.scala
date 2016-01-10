package Admin

import java.util.UUID
import Projects.{Project, ProjectService}
import com.greencatsoft.angularjs.core.Timeout
import com.greencatsoft.angularjs.{Filter, Angular, AbstractController, injectable}
import org.scalajs.dom.html.Image
import scala.concurrent.ExecutionContext.Implicits.global
import scala.scalajs.js
import org.scalajs.dom._
import scala.scalajs.js.{Object, Date}
import scala.scalajs.js.annotation.{JSExport, JSExportAll}
import scala.scalajs.js.JSConverters.JSRichGenTraversableOnce
import scala.util.{Failure, Success}

@JSExportAll
@injectable("adminController")
class AdminController(adminScope: AdminScope, timeout: Timeout, projectService: ProjectService) extends AbstractController[AdminScope](adminScope) {

  var isOpen = false
  var projects: Seq[Project] = Seq.empty
  adminScope.tags = Seq.empty[String].toJSArray
  adminScope.technologies = Seq.empty[String].toJSArray
  val newColumn0 = new Object().asInstanceOf[GalleryColumn]
  newColumn0.number = 0
  newColumn0.size = 100
  val newColumn1 = new Object().asInstanceOf[GalleryColumn]
  newColumn1.number = 1
  newColumn1.size = 33.33
  val newColumn2 = new Object().asInstanceOf[GalleryColumn]
  newColumn2.number = 2
  newColumn2.size = 33.33
  val newColumn3 = new Object().asInstanceOf[GalleryColumn]
  newColumn3.number = 3
  newColumn3.size = 33.33
  var galleryColumns = Seq(newColumn0, newColumn1, newColumn2, newColumn3).toJSArray

  def setNewColumn: Unit = {
    val newColumn = new Object().asInstanceOf[GalleryColumn]
    newColumn.number = galleryColumns.length
    var totalWidth: Double = 0
    galleryColumns.foreach { column =>
      totalWidth = totalWidth + column.size
    }
    if (totalWidth%100 < 95) {
      newColumn.size = 100 - totalWidth%100
    } else newColumn.size = 100
    console.log(adminScope.projects)
    if (galleryColumns.indexOf(newColumn) == -1) galleryColumns.push(newColumn)
  }

  projectService.findAll().onComplete  {
    case Success(projectsFound) =>
      timeout( () => {
        projects = setColumn(projectsFound)
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
          adminProject.columnNumber = project.columnNumber
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
    })
  }

  var heightcol1 = 0
  var heightcol2 = 0
  var heightcol3 = 0
  def setColumn(projects: Seq[Project]): Seq[Project] = {
    projects.map { project =>
      if (project.maxHeight.toDouble / project.maxWidth.toDouble < 0.4) {
        project
      } else {
        if (heightcol1 <= heightcol2 && heightcol1 <= heightcol3) {
          heightcol1 = (heightcol1 + (project.maxHeight * (100 / project.maxWidth.toDouble))).toInt
          project.copy(columnNumber = 1)
        }
        else if (heightcol2 <= heightcol3) {
          heightcol2 = (heightcol2 + (project.maxHeight * (100 / project.maxWidth.toDouble))).toInt
          project.copy(columnNumber = 2)
        }
        else {
          heightcol3 = (heightcol3 + (project.maxHeight * (100 / project.maxWidth.toDouble))).toInt
          project.copy(columnNumber = 3)
        }
      }
    }
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

  def addNewImage(): Unit = {
    console.log(adminScope.newImage)
    projectService.postImage(adminScope.newImage) onComplete {
      case Success(image) =>
        adminScope.newProject.image = image
    }
  }

  def refactorTech(technology: String): String = {
    val a = technology.substring(technology.lastIndexOf("/") + 1).replace(".svg", "")
    console.log(a)
    a
  }


}


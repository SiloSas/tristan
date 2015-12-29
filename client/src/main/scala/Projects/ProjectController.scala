package Projects


import java.util.UUID
import ClientClass.Project
import com.greencatsoft.angularjs.core.Timeout
import com.greencatsoft.angularjs.{Filter, Angular, AbstractController, injectable}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.scalajs.js
import org.scalajs.dom._
import scala.scalajs.js.Date
import scala.scalajs.js.annotation.{JSExport, JSExportAll}
import scala.scalajs.js.JSConverters.JSRichGenTraversableOnce
import scala.util.{Failure, Success}

@JSExportAll
@injectable("projectController")
class ProjectController(projectScope: ProjectScope, timeout: Timeout, projectService: ProjectService) extends AbstractController[ProjectScope](projectScope) {


  var projects: Seq[Project] = Seq.empty

  projectService.findAll().onComplete  {
    case Success(projectsFound) =>
      timeout( () => {
        projects = projectsFound
        projectScope.projects = setColumn(projects).toJSArray
      }, 0, true)
    case Failure(t: Throwable) =>
      console.log(throw t)
  }
  projectScope.index = 0
  var heightcol1 = 0
  var heightcol2 = 0
  var heightcol3 = 0

  def setColumn(projects: Seq[Project]): Seq[Project] = {
    projects.map { project =>
      if (project.maxHeight.toDouble / project.maxWidth.toDouble < 0.4) {
        project
      } else {
        if (heightcol1 <= heightcol2 && heightcol1 <= heightcol3) {
          console.log(heightcol1)
          heightcol1 = (heightcol1 + (project.maxHeight * (100 / project.maxWidth.toDouble))).toInt
          project.copy(columnNumber = 1)
        }
        else if (heightcol2 <= heightcol3) {
          console.log("yo")
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

  projectScope.projects = setColumn(projects).toJSArray

  projectScope.tags = Seq.empty[String].toJSArray

  projectScope.projects.map { project =>
    project.tags foreach { tag =>
      if (projectScope.tags.indexOf(tag) == -1)  projectScope.tags = projectScope.tags :+ tag
    }
    println(projectScope.tags)
    project
  }

  @JSExport
  def prevIndex(): Unit = {
    timeout(() => {
      if(projectScope.index > 0) projectScope.index = projectScope.index -1
      else projectScope.index = projectScope.projects.length -1
    }, 0, true)
  }
  @JSExport
  def nextIndex(): Unit = {
    timeout(() => {
      if(projectScope.index < projectScope.projects.length -1) projectScope.index = projectScope.index +1
      else projectScope.index = 0
    }, 0, true)
  }
  @JSExport
  def setIndex(index: Int): Unit = {
    timeout(() => {
      console.log(index)
      projectScope.index = index
    }, 0, true)
  }

  @JSExport
  def setIndexById(id: String): Unit = {
    timeout(() => {
      val index = projectScope.projects.indexOf(projectScope.projects.filter(_.id == id).head)
      projectScope.index = index
    }, 0, true)
  }

  @JSExport
  def filter(tag: String): Unit = {
    heightcol1 = 0
    heightcol2 = 0
    heightcol3 = 0
    projectScope.projects = setColumn(projects.filter(_.tags.indexOf(tag) > -1).toSeq).toJSArray
  }
  @JSExport
  def removeFilter(): Unit = {
    heightcol1 = 0
    heightcol2 = 0
    heightcol3 = 0
    projectScope.projects = setColumn(projects).toJSArray
  }

  window.onkeydown =  (event: KeyboardEvent) => {
    console.log(event.keyCode)
    if (event.keyCode == 37) prevIndex()
    if (event.keyCode == 39) nextIndex()
  }

}


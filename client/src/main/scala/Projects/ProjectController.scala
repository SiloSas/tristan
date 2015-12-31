package Projects


import java.util.UUID
import ClientClass.Project
import com.greencatsoft.angularjs.core.{RootScope, Timeout, Window}
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
class ProjectController(projectScope: ProjectScope, timeout: Timeout, projectService: ProjectService,
                        rootScope: RootScope, window: Window)
  extends AbstractController[ProjectScope](projectScope) {


  var projects: Seq[Project] = Seq.empty
  projectScope.tags = Seq.empty[String].toJSArray
  projectScope.index = 0
  var heightcol1 = 0
  var heightcol2 = 0
  var heightcol3 = 0

  var showCv = false
  var slider = false

  projectService.findAll().onComplete  {
    case Success(projectsFound) =>
      timeout( () => {
        projects = projectsFound
        projectScope.projects = setColumn(projects.map(setMaxSize)).toJSArray
        setTagsScope
      }, 0, true)
    case Failure(t: Throwable) =>
      console.log(throw t)
  }

  def setTagsScope: Unit = {
    projects.foreach { project =>
      project.tags foreach { tag =>
        if (projectScope.tags.indexOf(tag) == -1) projectScope.tags = projectScope.tags :+ tag
      }
    }
  }


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

  def setMaxSize(project: Project): Project = {
    val windowMaxwidth = window.innerWidth
    val windowMaxHeight = window.innerHeight
    val menuHeight = 100
    var newProject = project.copy()
    if (project.maxWidth > windowMaxwidth ) {
      console.log(windowMaxHeight)
      val newMaxHeight = project.maxHeight * (windowMaxwidth/project.maxWidth.toDouble)
      newProject = project.copy(maxHeight = newMaxHeight.toInt, maxWidth = windowMaxwidth)
    }
    if (newProject.maxHeight > windowMaxHeight ) {
      val newMaxWidth = newProject.maxWidth * ((windowMaxHeight-menuHeight)/newProject.maxHeight.toDouble)
      console.log(windowMaxHeight)
      newProject = newProject.copy(maxHeight = windowMaxHeight-menuHeight, maxWidth = newMaxWidth.toInt, isLandscape = false)
    }
    newProject
  }

  def resizeImages = (event: Event) => {
    projectScope.projects = projectScope.projects.map(setMaxSize)
  }

  window.addEventListener("resize", resizeImages)

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
    if (event.keyCode == 27) {
      timeout( () => {
        showCv = false
        slider = false
      }, 0, true)
    }

  }

  def refactorTech(technology: String): String = {
    val a = technology.substring(technology.lastIndexOf("/") + 1).replace(".svg", "")
    console.log(a)
    a
  }

}


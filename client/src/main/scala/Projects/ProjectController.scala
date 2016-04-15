package Projects


import com.greencatsoft.angularjs.core.{RootScope, SceService, Timeout, Window}
import com.greencatsoft.angularjs.{AbstractController, injectable}
import org.scalajs.dom.html._
import org.scalajs.dom.raw.KeyboardEvent
import org.scalajs.dom.{console, document}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.scalajs.js.JSConverters.JSRichGenTraversableOnce
import scala.scalajs.js.annotation.{JSExport, JSExportAll}
import scala.util.{Failure, Success}

@JSExportAll
@injectable("projectController")
class ProjectController(projectScope: ProjectScope, timeout: Timeout, projectService: ProjectService,
                        rootScope: RootScope, window: Window, sce: SceService)
  extends AbstractController[ProjectScope](projectScope) {


  var projects: Seq[Project] = Seq.empty
  projectScope.tags = Seq.empty[String].toJSArray
  projectScope.index = 0

//  document.getElementById("container").asInstanceOf[Html].tabIndex = 1
  document.getElementById("container").asInstanceOf[Html].focus()

  var inProgress = true
  var showCv = false
  var showContact = false
  var slider = false
  var limit = 10
  var preloader = document.getElementById("preloader")
  var scrollContainer = document.getElementsByTagName("md-content").item(0).asInstanceOf[Html]

  projectService.getContact() map { foundContact =>
    projectScope.contact = sce.trustAsHtml(foundContact)
  }

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

  def loadMoreProjects(): Unit = {
    val images = document.getElementsByTagName("project-min")
    val lastImageTop = if (images.length > 0)
      images.item(images.length - 1).asInstanceOf[Html].getBoundingClientRect().top else 0
    console.log(lastImageTop)
    console.log(scrollContainer.scrollTop)
    if (scrollContainer.scrollTop >= lastImageTop - 10 && lastImageTop != 0) timeout(() => limit = limit + 10)
  }

  def getProjects: Unit = {
    projectService.findAll().onComplete {
      case Success(projectsFound) =>
        timeout(() => {
          projects = projectsFound
          projectScope.projects = projects.toJSArray
          inProgress = false
          setTagsScope
        }, 0, true)
      case Failure(t: Throwable) =>
        console.log(throw t)
    }
  }

  def setTagsScope: Unit = {
    projects.foreach { project =>
      project.tags foreach { tag =>
        if (projectScope.tags.indexOf(tag) == -1) projectScope.tags = projectScope.tags :+ tag
      }
    }
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
    timeout( () => {
      projectScope.projects = projects.filter(_.tags.indexOf(tag) > -1).toJSArray
      projectScope.index = 0
    })
  }
  @JSExport
  def removeFilter(): Unit = {
    timeout( () => {
      projectScope.projects = projects.toJSArray
    })
  }

  window.onkeydown =  (event: KeyboardEvent) => {
    console.log(event.keyCode)
    if (event.keyCode == 37) prevIndex()
    if (event.keyCode == 39) nextIndex()
    if (event.keyCode == 27) {
      timeout( () => {
        if (showCv || showContact) {
          showCv = false
          showContact = false
        } else {
          slider = false
        }
      }, 0, true)
    }
  }

  def refactorTech(technology: String): String = {
    val a = technology.substring(technology.lastIndexOf("/") + 1).replace(technology.substring(technology.length()-4), "")
    a
  }

}


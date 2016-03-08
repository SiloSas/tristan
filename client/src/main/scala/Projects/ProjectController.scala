package Projects


import com.greencatsoft.angularjs.core.{RootScope, SceService, Timeout, Window}
import com.greencatsoft.angularjs.{AbstractController, injectable}
import org.scalajs.dom.html._
import org.scalajs.dom.raw.KeyboardEvent
import org.scalajs.dom.{Event, console, document}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.scalajs.js.JSConverters.JSRichGenTraversableOnce
import scala.scalajs.js.annotation.{JSExport, JSExportAll}
import scala.scalajs.js.timers._
import scala.util.{Failure, Success}

@JSExportAll
@injectable("projectController")
class ProjectController(projectScope: ProjectScope, timeout: Timeout, projectService: ProjectService,
                        rootScope: RootScope, window: Window, sce: SceService)
  extends AbstractController[ProjectScope](projectScope) {


  var projects: Seq[Project] = Seq.empty
  projectScope.tags = Seq.empty[String].toJSArray
  projectScope.index = 0

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

  def waitForPreload(): Unit = {
    preloader = document.getElementById("preloader")
    val images = preloader.getElementsByTagName("img")
    val max = if (limit < projects.length -1) limit else projects.length -1
    if (images.length < max) timeout( () => waitForPreload(), 150)
    else {
      def isAllReady(i: Int) {
        val image = images.item(i).asInstanceOf[Image]
        if (image.complete) {
          if (i < images.length -1 && i < max) timeout(() => isAllReady(i+1), 20)
          else {
//            console.log("ready")
            timeout(() => {
              inProgress = false
              timeout(() => {
                scrollContainer = document.getElementsByTagName("md-content").item(0).asInstanceOf[Html]
                var timer2 = setTimeout(50)(loadMoreProjects())
                val waitForLoadMoreProjects = (event: Event) => {
                  clearTimeout(timer2)
                  if (limit < projects.length -1) timer2 = setTimeout(50)(loadMoreProjects())
                }
                scrollContainer.onscroll = waitForLoadMoreProjects
              }, 0)
            })
          }
        } else {
          console.log("not ready")
          timeout(() => waitForPreload(), 250)
        }
      }
      isAllReady(0)
    }
  }


  def getProjects: Unit = {
    projectService.findAll().onComplete {
      case Success(projectsFound) =>
        timeout(() => {
          projects = projectsFound
          timeout(() => projectScope.projects = projects.map(setMaxSize).toJSArray)

          waitForPreload()
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
    timeout( () => {
      projectScope.projects = projects.filter(_.tags.indexOf(tag) > -1).map(setMaxSize).toJSArray
      projectScope.index = 0
    })
  }
  @JSExport
  def removeFilter(): Unit = {
    timeout( () => {
      projectScope.projects = projects.map(setMaxSize).toJSArray
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


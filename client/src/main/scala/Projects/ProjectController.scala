package Projects


import java.util.UUID
import Shared.Project
import com.greencatsoft.angularjs.core.Timeout
import com.greencatsoft.angularjs.{Filter, Angular, AbstractController, injectable}

import scala.scalajs.js
import org.scalajs.dom._
import scala.scalajs.js.Date
import scala.scalajs.js.annotation.{JSExport, JSExportAll}
import scala.scalajs.js.JSConverters.JSRichGenTraversableOnce

@JSExportAll
@injectable("projectController")
class ProjectController(projectScope: ProjectScope, timeout: Timeout) extends AbstractController[ProjectScope](projectScope) {
  val project1 = Project(
    id = UUID.randomUUID().toString,
    name = "last night",
    description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.",
    image = "assets/images/lastNight.jpg",
    tags = js.Array("environement"),
    technologies = js.Array("assets/images/3dsmax.svg", "assets/images/zbrush.svg", "assets/images/maya.svg", "assets/images/photoshop.svg"),
    date = new Date(), isLandscape = true, maxHeight = 403, maxWidth = 716, column = 0)
  val project = Project(id = UUID.randomUUID().toString, name = "sous l'objectif",
    description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.",
    image = "assets/images/sousObjectif.png",
    technologies = js.Array("assets/images/3dsmax.svg", "assets/images/zbrush.svg", "assets/images/maya.svg", "assets/images/photoshop.svg"),
  tags = js.Array("characters"), date = new Date(), isLandscape = true, maxHeight = 449, maxWidth = 798, column = 0)
  val project4 = Project(id = UUID.randomUUID().toString, name = "Portrait",
    technologies = js.Array("assets/images/3dsmax.svg", "assets/images/zbrush.svg", "assets/images/maya.svg", "assets/images/photoshop.svg"),
    description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.",
    image = "assets/images/portrait.png",
    tags = js.Array("characters"), date = new Date(), isLandscape = false, maxHeight = 769, maxWidth = 476, column = 0)
  val project3 = Project(id = UUID.randomUUID().toString, name = "avalon",
    technologies = js.Array("assets/images/3dsmax.svg", "assets/images/zbrush.svg", "assets/images/maya.svg", "assets/images/photoshop.svg"),
    description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.",
    image = "assets/images/avalon.jpg",
  tags = js.Array("environement"), date = new Date(), isLandscape = true, maxHeight = 449, maxWidth = 798, column = 0)
  val project2 = Project(id = UUID.randomUUID().toString, name = "chameleon",
    technologies = js.Array("assets/images/3dsmax.svg", "assets/images/zbrush.svg", "assets/images/maya.svg", "assets/images/photoshop.svg"),
    description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.",
    image = "assets/images/chameleon.jpg",
  tags = js.Array("characters"), date = new Date(), isLandscape = false, maxHeight = 403, maxWidth = 537, column = 0 )
  val project5 = Project(id = UUID.randomUUID().toString, name = "Landscape",
    technologies = js.Array("assets/images/3dsmax.svg", "assets/images/zbrush.svg", "assets/images/maya.svg", "assets/images/photoshop.svg"),
    description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.",
    image = "assets/images/landscape.png",
    tags = js.Array("characters"), date = new Date(), isLandscape = true, maxHeight = 624, maxWidth = 1680, column = 0)
  val project6 = Project(id = UUID.randomUUID().toString, name = "Ant",
    technologies = js.Array("assets/images/3dsmax.svg", "assets/images/zbrush.svg", "assets/images/maya.svg", "assets/images/photoshop.svg"),
    description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.",
    image = "assets/images/ant.jpg",
    tags = js.Array("characters"), date = new Date(), isLandscape = false, maxHeight = 769, maxWidth = 1025, column = 0)
  val project7 = Project(id = UUID.randomUUID().toString, name = "Head",
    technologies = js.Array("assets/images/3dsmax.svg", "assets/images/zbrush.svg", "assets/images/maya.svg", "assets/images/photoshop.svg"),
    description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.",
    image = "assets/images/head.jpg",
    tags = js.Array("characters"), date = new Date(), isLandscape = false, maxHeight = 769, maxWidth = 1025, column = 0)

  var projects = Seq(project, project1, project2, project3, project4, project5, project6, project7)

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
          project.copy(column = 1)
        }
        else if (heightcol2 <= heightcol3) {
          console.log("yo")
          heightcol2 = (heightcol2 + (project.maxHeight * (100 / project.maxWidth.toDouble))).toInt
          project.copy(column = 2)
        }
        else {
          heightcol3 = (heightcol3 + (project.maxHeight * (100 / project.maxWidth.toDouble))).toInt
          project.copy(column = 3)
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


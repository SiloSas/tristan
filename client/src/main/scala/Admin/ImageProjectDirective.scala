package Admin


import com.greencatsoft.angularjs._
import com.greencatsoft.angularjs.core.{RootScope, Timeout, Window}
import org.scalajs.dom.html.{Html, Image}
import org.scalajs.dom.raw.Event
import org.scalajs.dom.{Element, document}

import scala.scalajs.js.Array
import scala.scalajs.js.annotation.JSExport
import scala.scalajs.js.timers._


@injectable("imageProject")
class ImageProjectDirective(timeout: Timeout, angularWindow: Window, rootScope: RootScope) extends ClassDirective {
  var parentWidth = 0.0
  var elementsWidth = 0.0
  var height = 450.0
  var baseHeight = 450.0
  var rowElements = new Array[Image]()
  var images = document.getElementsByClassName("mainImage")
  val ratio: Double = 1.0 /*js.eval("window.devicePixelRatio").toString.toDouble*/

  def resizeRow(): Unit = {
    height = baseHeight * (parentWidth / elementsWidth)
    rowElements.foreach { rowElem =>
      val elemRatio = height / rowElem.parentElement.getAttribute("naturalHeight").toDouble
      rowElem.style.height = height + "px"
      rowElem.style.width = (rowElem.parentElement.getAttribute("naturalWidth").toDouble * elemRatio) + "px"
    }
    rowElements = new Array[Image]()
    elementsWidth = 0.0
  }

  def calculeHeight(): Unit = {

      val length = images.length

      for (i <- 0 to (length - 1)) {
        if (angularWindow.innerWidth > 700) {
        val elem = images.item(i).asInstanceOf[Image]
        val heightRatio = baseHeight / elem.parentElement.getAttribute("naturalHeight").toDouble
        org.scalajs.dom.console.log(heightRatio)
        val totalWidth = (elem.parentElement.getAttribute("naturalWidth").toDouble * heightRatio) + elementsWidth
        totalWidth match {
          case under if under <= parentWidth - (parentWidth * 20 / 100) =>
            elementsWidth = totalWidth
            rowElements.push(elem)
            if (i == length - 1) resizeRow()

          case _ =>
            elementsWidth = totalWidth
            rowElements.push(elem)
            resizeRow()
        }
      } else {
          images.item(i).asInstanceOf[Image].style.width = "100%"
      }
    }
  }

  def getParentWidth: Double = {
    val newParentWidth = angularWindow.innerWidth
    Math.round(newParentWidth - 20)
  }


  @JSExport
  def setBaseHeight(double: Double): Unit = {
    parentWidth = getParentWidth
    baseHeight = double
    height = double
  }
  var timer = setTimeout(600)()
  val changeHeight = (event: Event) => {
    clearTimeout(timer)
    if (angularWindow.innerWidth > 960) {
      timeout( () => {
        parentWidth = document.getElementsByClassName("image-project").item(0).asInstanceOf[Html].getBoundingClientRect().width
        timer = setTimeout(600)(calculeHeight())
      }, 50)

    }
  }

  var maybeChangeHeight = (event: Event) => {
    val elemWidth = getParentWidth
    if(elemWidth != parentWidth) {
      parentWidth = elemWidth
      calculeHeight()
    }
  }

  angularWindow.addEventListener("resize", maybeChangeHeight)

  def resize(): Unit = {
    timeout(() => {
      val element = document.getElementsByClassName("image-project").item(0).asInstanceOf[Html]
      parentWidth = getParentWidth
      images = element.getElementsByClassName("mainImage")
      calculeHeight()
    }, 300, true)
  }

  override def link(scopeType: ScopeType, elements: Seq[Element], attributes: Attributes): Unit = {
    elements.map(_.asInstanceOf[Html]).foreach { elem =>
      if (angularWindow.innerWidth > 700) {
        rootScope.$watch("controller.limit", resize())
        resize()
      } else {
        elem.style.width = "100%"
      }
    }
  }
}
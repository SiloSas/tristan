package Admin


import com.greencatsoft.angularjs._
import com.greencatsoft.angularjs.core.Timeout
import org.scalajs.dom.console
import org.scalajs.dom.document
import org.scalajs.dom.window
import org.scalajs.dom.Element
import org.scalajs.dom.html.{Image, Html}
import org.scalajs.dom.raw.Event

import scala.scalajs.js
import scala.scalajs.js.Array
import scala.scalajs.js.annotation.JSExport
import scala.scalajs.js.timers._

@injectable("imageProject")
class ImageProjectDirective(timeout: Timeout) extends ClassDirective {
  var parentWidth = 0.0
  var elementsWidth = 0.0
  var height = 450.0
  var baseHeight = 450.0
  var rowElements = new Array[Html]()
  var images = document.getElementsByTagName("img")


  def calculeHeight(): Unit = {
    var i = 0
    val length = images.length
    console.log(length)
    console.log(images)
    for (i <- 0 to (length - 1)) {
      val elem = images.item(i).asInstanceOf[Html]
      elem.style.height = baseHeight + "px"
      val totalWidth = elem.getBoundingClientRect().width + elementsWidth
      totalWidth match {
        case under if under <= parentWidth - (parentWidth * 20 / 100) =>
          elementsWidth = totalWidth
          rowElements.push(elem)
          if(i == length -1) {
            height = elem.getBoundingClientRect().height * (parentWidth  / elementsWidth)
            var checkWidth = 0.0
            rowElements.foreach{ rowElem =>
              rowElem.style.height = height + "px"
              checkWidth = checkWidth + rowElem.getBoundingClientRect().width
            }
            if (checkWidth > parentWidth || checkWidth < parentWidth - 2) {
              resize(0.1, checkWidth)
            }
            rowElements = new js.Array[Html]()
            elementsWidth = 0.0
          }
        case complete if complete >= parentWidth - (parentWidth * 20 / 100) && complete <= parentWidth + (parentWidth * 20 / 100) =>
          elementsWidth = totalWidth
          rowElements.push(elem)
          height = baseHeight * (parentWidth / elementsWidth)
          var checkWidth = 0.0
          rowElements.foreach{ rowElem =>
            rowElem.style.height = height + "px"
            checkWidth = checkWidth + rowElem.getBoundingClientRect().width
          }
          if (checkWidth > parentWidth || checkWidth < parentWidth - 2) {
           resize(0.1, checkWidth)
          }

          elementsWidth = 0.0
          rowElements = new js.Array[Html]()
        case _ =>
          height = baseHeight * (parentWidth / elementsWidth)
          rowElements.foreach(_.style.height = height + "px")
          if(i == length -1) {
            height = baseHeight * (parentWidth / elementsWidth)
            var checkWidth = 0.0
            rowElements.foreach{ rowElem =>
              rowElem.style.height = height + "px"
              checkWidth = checkWidth + rowElem.getBoundingClientRect().width
            }
            if (checkWidth > parentWidth || checkWidth < parentWidth - 2) {
              resize(0.1, checkWidth)
            }
            rowElements = new js.Array[Html]()
            elementsWidth = 0.0
          } else {
            rowElements = new js.Array[Html]()
            rowElements.push(elem)
            elementsWidth = elem.getBoundingClientRect().width
          }
      }
    }
  }

  def getParentWidth: Double = {
    val newParentWidth = document.getElementsByClassName("image-project").item(0).asInstanceOf[Html].getBoundingClientRect().width
    val windowWidth = window.innerWidth
    //if (newParentWidth <= (windowWidth - 15)) newParentWidth
    //else
    windowWidth -15
  }

  def resize(i:Double, checkWidthBase: Double): Unit = {
    var checkWidth = checkWidthBase
    parentWidth = getParentWidth
    //console.log(checkWidth)
    console.log(parentWidth)
    //console.log(height)
    height = height * ((parentWidth - i) / checkWidth)
//    console.log(height)
//    console.log(height * parentWidth / checkWidth)
    checkWidth = 0.0
    rowElements.foreach{ rowElem =>
      rowElem.style.height = height + "px"
      checkWidth = checkWidth + rowElem.getBoundingClientRect().width
    }
    console.log(checkWidth)
    if(checkWidth > parentWidth) {
      resize(i + 0.2, checkWidth)
    }
  }


  @JSExport
  def recalculHeight(): Unit = {
    timeout( () => {
      calculeHeight()
    }, 10)
  }

  var timer1 = setTimeout(600)(calculeHeight())
  @JSExport
  def setBaseHeight(double: Double): Unit = {
    parentWidth = getParentWidth
    baseHeight = double
    height = double
    timer1 = setTimeout(600)(calculeHeight())
  }
  var timer = setTimeout(600)(calculeHeight())
  val changeHeight = (event: Event) => {
    clearTimeout(timer)
    if (window.innerWidth > 960) {
      timeout( () => {
        parentWidth = document.getElementsByClassName("image-project").item(0).asInstanceOf[Html].getBoundingClientRect().width
        timer = setTimeout(600)(calculeHeight())
      }, 50)

    }
  }

  window.addEventListener("resize", changeHeight)
  var timer2 = setTimeout(600)(calculeHeight())
  var maybeChangeHeight = (event: Event) => {
    clearTimeout(timer2)
    val elemWidth = getParentWidth
    if(elemWidth != parentWidth) {
      parentWidth = elemWidth
      timer2 = setTimeout(600)(calculeHeight())
    }
  }

  override def link(scopeType: ScopeType, elements: Seq[Element], attributes: Attributes): Unit = {
    elements.map(_.asInstanceOf[Html]).foreach { elem =>
      def resize(): Unit = {
        timeout(() => {
          val element = document.getElementsByClassName("image-project").item(0).asInstanceOf[Html]
          parentWidth = getParentWidth
          images = element.getElementsByTagName("img")
          if (images.item(images.length -1).asInstanceOf[Image].complete)  calculeHeight()
          else resize()
        }, 1500, true)
        scopeType.$watch("projects", calculeHeight())
      }
      resize()
      elem.addEventListener("resize", maybeChangeHeight)
    }
  }
}
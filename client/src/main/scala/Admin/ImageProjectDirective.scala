package Admin


import com.greencatsoft.angularjs._
import com.greencatsoft.angularjs.core.{Window, Timeout}
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
class ImageProjectDirective(timeout: Timeout, angularWindow: Window) extends ClassDirective {
  var parentWidth = 0.0
  var elementsWidth = 0.0
  var height = 450.0
  var baseHeight = 450.0
  var rowElements = new Array[Html]()
  var images = document.getElementsByTagName("img")
  val ratio: Double = js.eval("window.devicePixelRatio").toString.toDouble


  def calculeHeight(): Unit = {
    var i = 0
    val length = images.length
    console.log(length)
    console.log(images)
    for (i <- 0 to (length - 1)) {
      val elem = images.item(i).asInstanceOf[Html]
      elem.style.height = baseHeight + "px"
      val totalWidth = elem.getBoundingClientRect().width * ratio + elementsWidth
      totalWidth match {
        case under if under <= parentWidth - (parentWidth * 20 / 100) =>
          elementsWidth = totalWidth
          rowElements.push(elem)
          if(i == length -1) {
            height = elem.getBoundingClientRect().height * (parentWidth  / elementsWidth)
            var checkWidth = 0.0
            rowElements.foreach{ rowElem =>
              rowElem.style.height = height + "px"
              checkWidth = checkWidth + rowElem.getBoundingClientRect().width * ratio
            }
            if (checkWidth > parentWidth || checkWidth < parentWidth - 2) {
              resize(0, checkWidth)
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
            checkWidth = checkWidth + rowElem.getBoundingClientRect().width * ratio
          }
          if (checkWidth > parentWidth || checkWidth < parentWidth - 2) {
           resize(0, checkWidth)
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
              checkWidth = checkWidth + rowElem.getBoundingClientRect().width * ratio
            }
            if (checkWidth > parentWidth || checkWidth < parentWidth - 2) {
              resize(0, checkWidth)
            }
            rowElements = new js.Array[Html]()
            elementsWidth = 0.0
          } else {
            rowElements = new js.Array[Html]()
            rowElements.push(elem)
            elementsWidth = elem.getBoundingClientRect().width * ratio
          }
      }
    }
  }

  def getParentWidth: Double = {
    //val newParentWidth = document.getElementsByClassName("image-project").item(0).asInstanceOf[Html].getBoundingClientRect().width
    val windowWidth = angularWindow.innerWidth
    //if (newParentWidth <= (windowWidth - 15)) newParentWidth
    //else * window.devicePixelRatio
    console.log(ratio)
    Math.round(document.body.clientWidth * ratio -15)
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
    if(checkWidth > parentWidth) {
      resize(i + 1, checkWidth)
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
    if (angularWindow.innerWidth > 960) {
      timeout( () => {
        parentWidth = document.getElementsByClassName("image-project").item(0).asInstanceOf[Html].getBoundingClientRect().width
        timer = setTimeout(600)(calculeHeight())
      }, 50)

    }
  }

  angularWindow.addEventListener("resize", changeHeight)
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
          /* if (!theImage.get(0).complete) {
        return false;
    }
    else if (theImage.height() === 0) {
        return false;
    }

    return true;*/
          val element = document.getElementsByClassName("image-project").item(0).asInstanceOf[Html]
          parentWidth = getParentWidth
          images = element.getElementsByTagName("img")
          if (images.item(images.length -1).asInstanceOf[Image].height == 0) resize()
          else if (!images.item(images.length -1).asInstanceOf[Image].complete)  resize()
          else calculeHeight()
        }, 1500, true)
        scopeType.$watch("projects", calculeHeight())
      }
      resize()
      elem.addEventListener("resize", maybeChangeHeight)
    }
  }
}
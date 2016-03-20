package Admin


import com.greencatsoft.angularjs._
import com.greencatsoft.angularjs.core.{RootScope, Timeout, Window}
import org.scalajs.dom.html.{Html, Image}
import org.scalajs.dom.raw.Event
import org.scalajs.dom.{Element, document}

import scala.scalajs.js
import scala.scalajs.js.Array
import scala.scalajs.js.annotation.JSExport
import scala.scalajs.js.timers._


@injectable("imageProject")
class ImageProjectDirective(timeout: Timeout, angularWindow: Window, rootScope: RootScope) extends ClassDirective {
  var parentWidth = 0.0
  var elementsWidth = 0.0
  var height = 450.0
  var baseHeight = 450.0
  var rowElements = new Array[Html]()
  var images = document.getElementsByClassName("mainImage")
  val ratio: Double = 1.0 /*js.eval("window.devicePixelRatio").toString.toDouble*/


  def calculeHeight(): Unit = {
    var i = 0
    val length = images.length
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
              rowElem.style.height = math.floor(height) + "px"
              checkWidth = checkWidth + rowElem.getBoundingClientRect().width * ratio
            }
            if (checkWidth >= parentWidth || checkWidth < parentWidth - 3) {
              resize(1, checkWidth)
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
            rowElem.style.height = math.floor(height) + "px"
            checkWidth = checkWidth + rowElem.getBoundingClientRect().width * ratio
          }
          if (checkWidth >= parentWidth || checkWidth < parentWidth - 3) {
            resize(1, checkWidth)
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
              rowElem.style.height = math.floor(height) + "px"
              checkWidth = checkWidth + rowElem.getBoundingClientRect().width * ratio
            }
            if (checkWidth >= parentWidth || checkWidth < parentWidth - 3) {
              resize(1, checkWidth)
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
    val newParentWidth = document.getElementsByClassName("image-project").item(0).asInstanceOf[Html].getBoundingClientRect().width
    val windowWidth = angularWindow.innerWidth
    Math.round(newParentWidth - 5)
  }

  def resize(i:Double, checkWidthBase: Double): Unit = {
    var checkWidth = checkWidthBase
    parentWidth = getParentWidth
    //console.log(height)
    height = height * ((parentWidth - i) / checkWidth)
//    console.log(height)
//    console.log(height * parentWidth / checkWidth)
    checkWidth = 0.0
    rowElements.foreach{ rowElem =>
      rowElem.style.height = math.floor(height) + "px"
      checkWidth = checkWidth + rowElem.getBoundingClientRect().width
    }
    if(checkWidth > parentWidth) {
      resize(i + 1, checkWidth)
    }
  }


  @JSExport
  def recalculHeight(): Unit = {
    timeout( () => {
      if (angularWindow.innerWidth > 700) {
        calculeHeight()
      }
    }, 10)
  }

//  var timer1 = setTimeout(600)(calculeHeight())
  @JSExport
  def setBaseHeight(double: Double): Unit = {
    parentWidth = getParentWidth
    baseHeight = double
    height = double
//    timer1 = setTimeout(600)(calculeHeight())
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

  if (angularWindow.innerWidth > 700) {
    angularWindow.addEventListener("resize", maybeChangeHeight)
  }
  var timer2 = setTimeout(600)()
  var maybeChangeHeight = (event: Event) => {
    clearTimeout(timer2)
    val elemWidth = getParentWidth
    if(elemWidth != parentWidth) {
      parentWidth = elemWidth
      timer2 = setTimeout(600)(calculeHeight())
    }
  }
  def resize(): Unit = {
    timeout(() => {
      val element = document.getElementsByClassName("image-project").item(0).asInstanceOf[Html]
      parentWidth = getParentWidth
      images = element.getElementsByClassName("mainImage")
      var allImagesReady = true
      calculeHeight()
      def isAllReady(i: Int) {
        val image = images.item(i).asInstanceOf[Image]
        if (image.getBoundingClientRect().height > 30 && image.getBoundingClientRect().width > 10) {
          if (i < images.length -1) timeout(() => isAllReady(i+1), 10)
          else {
//            console.log("ready")
            calculeHeight()
          }
        } else {
//          console.log("not readyyyyy")
          allImagesReady = false
          timeout(() => resize(), 400)
        }
      }
      isAllReady(0)
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
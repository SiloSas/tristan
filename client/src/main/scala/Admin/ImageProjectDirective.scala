package Admin


import com.greencatsoft.angularjs._
import com.greencatsoft.angularjs.core.Timeout
import org.scalajs.dom._
import org.scalajs.dom.html.Html

import scala.scalajs.js
import scala.scalajs.js.Array
import scala.scalajs.js.annotation.JSExport

@injectable("imageProject")
class ImageProjectDirective(timeout: Timeout) extends ElementDirective {
  var parentWidth = 0.0
  var elementsWidth = 0.0
  var height = 400.0
  val baseHeight = 400.0
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
        case under if under <= parentWidth - (parentWidth * 10 / 100) =>
          console.log(1)
          elementsWidth = totalWidth
          rowElements.push(elem)
          if(i == length -1) {
            height = elem.getBoundingClientRect().height * (parentWidth  / elementsWidth)
            rowElements.foreach(_.style.height = height + "px")
            rowElements = new js.Array[Html]()
            elementsWidth = 0
          }
        case complete if complete >= parentWidth - (parentWidth * 10 / 100) && complete <= parentWidth + (parentWidth * 20 / 100) =>
          elementsWidth = totalWidth
          rowElements.push(elem)
          console.log(2)
          console.log(rowElements)
          height = baseHeight.toDouble * ((parentWidth - 5) / elementsWidth)
          console.log(parentWidth)
          console.log(parentWidth / elementsWidth)
          console.log(height)
          rowElements.foreach(_.style.height = height + "px")
          elementsWidth = 0
          rowElements = new js.Array[Html]()
          console.log(3)
        case _ =>
          height = baseHeight.toDouble * ((parentWidth - 5) / elementsWidth)
          rowElements.foreach(_.style.height = height + "px")
          if(i == length -1) {
            height = baseHeight.toDouble * ((parentWidth - 5) / elementsWidth)
            rowElements.foreach(_.style.height = height + "px")
            rowElements = new js.Array[Html]()
            elementsWidth = 0
          } else {
            rowElements = new js.Array[Html]()
            rowElements.push(elem)
            elementsWidth = elem.clientWidth
            console.log(4)
          }
      }
    }
  }

  @JSExport
  def recalculHeight(): Unit = {
    timeout( () => {
      calculeHeight()
    }, 10)
  }

  override def link(scopeType: ScopeType, elements: Seq[Element], attributes: Attributes): Unit = {
    elements.map(_.asInstanceOf[Html]).foreach { elem =>
      timeout( () => {
        parentWidth = elem.parentNode.asInstanceOf[Html].clientWidth
        images = elem.getElementsByTagName("img")
        calculeHeight()
      }, 1000, true)
    }
  }
}
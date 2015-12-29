package Projects

import com.greencatsoft.angularjs.core.{Timeout, Window}
import com.greencatsoft.angularjs.{Attributes, ClassDirective, injectable}
import org.scalajs.dom.Element
import org.scalajs.dom._
import org.scalajs.dom.html.Html
import org.scalajs.dom.raw.Event

import scala.scalajs.js.annotation.JSExport

@JSExport
@injectable("slider")
class SliderDirective(window: Window, timeout: Timeout) extends ClassDirective {

  override def link(scope: ScopeType, elements: Seq[Element], attrs: Attributes): Unit = {
    elements.headOption.map(_.asInstanceOf[Html]) foreach { element =>
      def setNewLineHeight(): Unit = {
         element.style.lineHeight = element.getBoundingClientRect().height.toString + "px"
       }

      timeout (fn = () => {
        setNewLineHeight()
      }, 50, false)

      setNewLineHeight()
      val changeHeight = (event: Event) =>
        setNewLineHeight()
      window.addEventListener("resize", changeHeight)

      scope.$on("$destroy", () => {
        window.removeEventListener("resize", changeHeight)
      })
    }
  }
}

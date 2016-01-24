package Projects

import com.greencatsoft.angularjs.core.{Timeout, Window}
import com.greencatsoft.angularjs.extensions.{ModalOptions, ModalService}
import com.greencatsoft.angularjs.{ClassDirective, injectable}

import scala.scalajs.js
import scala.scalajs.js.Object
import scala.scalajs.js.annotation.JSExport

@JSExport
@injectable("contact")
class ContactDirective(window: Window, timeout: Timeout, modalService: ModalService) extends ClassDirective {

  def openModal = {
    var options = new Object().asInstanceOf[ModalOptions]
    options.templateUrl = "assets/templates/Projects/contactModal.html"
    modalService.open(options)
  }

}

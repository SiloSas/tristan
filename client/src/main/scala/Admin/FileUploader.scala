package Admin

import com.greencatsoft.angularjs.injectable

import scala.concurrent.Future
import scala.scalajs.js

@injectable("ImageFactory")
trait FileUploader extends js.Object {
  def postImage(image: js.Any): js.Any = js.native
}
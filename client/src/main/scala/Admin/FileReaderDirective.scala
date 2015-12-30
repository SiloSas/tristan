package Admin


import Projects.ProjectService
import com.greencatsoft.angularjs.core.Timeout
import com.greencatsoft.angularjs.extensions.{FileUploadConfig, FileUpload}
import com.greencatsoft.angularjs.{Angular, AttributeDirective, Attributes, injectable}
import org.scalajs.dom._
import org.scalajs.dom.html.Input
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.Success

@injectable("fileReader")
class FileReaderDirective(projectService: ProjectService, timeout: Timeout, fileUploader: FileUploader) extends AttributeDirective {

  override def link(scopeType: ScopeType, elements: Seq[Element], attributes: Attributes): Unit = {
    elements.foreach { elem =>
      Angular.element(elem).bind("change", (event: Event) => {
        val a = fileUploader.postImage(event.target.asInstanceOf[Input].files.item(0))
        console.log(a)
        /*onComplete {
          case Success(url) =>
            timeout( () => {
              scopeType.asInstanceOf[AdminScope].newProject.image = url
            }, 0, true)*/
//        }
        /*val reader = new FileReader()
        reader.onload = (loadEvent: Event) => {
          timeout( () => {
            console.log(loadEvent.target)
//            scopeType.asInstanceOf[AdminScope].newProject.image = loadEvent.target.res
            scopeType.asInstanceOf[AdminScope].newProject.image = loadEvent.target.asInstanceOf[FileReader].result.toString
          }, 0, true)
        }
        reader.readAsDataURL(event.target.asInstanceOf[Input].files.item(0))*/
       /* projectService.postImage(event.target.asInstanceOf[Input].files.item(0)) onComplete {
          case Success(image) =>
            timeout( () => {
              console.log(image)
            }, 0, true)
        }*/
      })
    }
  }
}




/*angular.module('bodeaApp').directive('appFilereader', function($q, $parse) {
    var slice = Array.prototype.slice;

    return {
        restrict: 'A',
        require: '?ngModel',
        link: function(scope, element, attrs) {
            var model = $parse(attrs.appFilereader);
            var modelSetter = model.assign;

            element.bind('change', function(){
                scope.$apply(function(){
                    modelSetter(scope, element[0].files[0]);
                });
            });

        } //link
    }; //return
});*/
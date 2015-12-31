import com.greencatsoft.angularjs.core.{Route, RouteProvider}
import com.greencatsoft.angularjs.{inject, Config}


object RoutingConfig extends Config {

  @inject
  var routeProvider: RouteProvider = _

  override def initialize() {

    routeProvider
      .when(path = "/",
        route = Route(templateUrl = "/assets/templates/main.html", title = "Main"))
      .when(
        path = "/contact",
        route = Route(
          templateUrl = "/assets/templates/Contact/contact.html",
          title = "Contact"))

  }
}

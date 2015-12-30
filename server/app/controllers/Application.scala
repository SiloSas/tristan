package controllers

import play.api.mvc._

object Application extends Controller {

  def index = Action {
    Ok(views.html.index("SharedMessages.itWorks"))
  }

  def admin = Action {
    Ok(views.html.admin(""))
  }

}

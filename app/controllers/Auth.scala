package controllers

import play.api.mvc._
import jp.t2v.lab.play2.auth._
import model.Guest
import views.html


object OptionalAuth extends Controller with Auth with AuthConfigImpl {

  def app = optionalUserAction {
    maybeUser => implicit request =>
      val user: User = maybeUser.getOrElse(Guest)
      Ok(html.user.app()).withCookies(Cookie("user", s"""{'username': '${user.name}', 'role': '${user.permission}' }"""))
  }

}
package controllers

import play.api.data._
import play.api.data.Forms._
import views._
import play.api.mvc._
import jp.t2v.lab.play2.auth._


object Application extends Controller with LoginLogout with AuthConfigImpl {

  val loginForm = Form {
    mapping("username" -> text, "password" -> text)(model.User.authenticate)(_.map(u => (u.name, "")))
      .verifying("Invalid username or password", result => result.isDefined)
  }

  val registryForm = Form {
    mapping("username" -> text, "password" -> text)(model.User.createUser)(_.map(u => (u.name, "")))
      .verifying("Invalid username or password", result => result.isDefined)
  }

  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  def login = Action {
    implicit request =>
      Ok(html.login(loginForm))
  }

  def register = Action {
    implicit request =>
      Ok(html.register(registryForm))
  }

  def registerForm = Action {
    implicit request =>
      registryForm.bindFromRequest.fold(
        formWithErrors => BadRequest(html.register(formWithErrors)),
        user => gotoLoginSucceeded(user.get.name)
      )
  }

  def logout = Action {
    implicit request =>
      gotoLogoutSucceeded.flashing(
        "success" -> "You've been logged out"
      )
  }

  def authenticate = Action {
    implicit request =>
      loginForm.bindFromRequest.fold(
        formWithErrors => BadRequest(html.login(formWithErrors)),
        user => gotoLoginSucceeded(user.get.name)
      )
  }

  def test = Action {
    Ok(views.html.test("test angualar ."))
  }

  def hands = Action {
    Ok(views.html.hands("test angualar ."))
  }

}
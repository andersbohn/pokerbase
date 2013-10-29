package controllers

import views._
import play.api.mvc._
import jp.t2v.lab.play2.auth._
import model.NormalUser


object Main extends Controller with AuthElement with AuthConfigImpl {

  import scala.concurrent.ExecutionContext.Implicits.global


  // Ordinary logged in :
  def index = authorizedAction(NormalUser) {
    user => implicit request =>
      val title = s"message main $user"
      Ok(html.user.index(title))
  }



  def test = Action {
    Ok(views.html.test("test angualar ."))
  }

  def hands = Action {
    Ok(views.html.hands("test angualar ."))
  }

}
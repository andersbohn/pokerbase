package controllers

import play.api.mvc._
import play.api.mvc.Results._
import jp.t2v.lab.play2.auth._
import scala.reflect.{ClassTag, classTag}
import model.{User, Permission}
import play.Logger

trait AuthConfigImpl extends AuthConfig {

  type Id = String

  type User = model.User

  type Authority = Permission

  /**
   * A `ClassManifest` is used to retrieve an id from the Cache API.
   * Use something like this:
   */
  val idTag: ClassTag[Id] = classTag[Id]


  val sessionTimeoutInSeconds: Int = 3600

  def resolveUser(id: Id): Option[User] = User.findUserByName(id)

  /**
   * Where to redirect the user after a successful login.
   */
  def loginSucceeded(request: RequestHeader): Result = Redirect(routes.Main.index)

  /**
   * Where to redirect the user after logging out
   */
  def logoutSucceeded(request: RequestHeader): Result = Redirect(routes.Application.index)

  /**
   * If the user is not logged in and tries to access a protected resource then redirect them as follows:
   */
  def authenticationFailed(request: RequestHeader): Result = Redirect(routes.Application.login)

  /**
   * If authorization failed (usually incorrect password) redirect the user as follows:
   */
  def authorizationFailed(request: RequestHeader): Result = Forbidden("no permission")

  /**
   * A function that determines what `Authority` a user has.
   * You should alter this procedure to suit your application.
   */
  def authorize(user: User, authority: Authority): Boolean = {
    // FIXME abj impl this
    Logger.info(s"authorize $user as $authority -> true")
    true
  }

  /**
   * Whether use the secure option or not use it in the cookie.
   * However default is false, I strongly recommend using true in a production.
   */
  override lazy val cookieSecureOption: Boolean = play.api.Play.current.configuration.getBoolean("auth.cookie.secure").getOrElse(true)

}
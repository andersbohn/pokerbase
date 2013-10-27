package controllers

import play.api.mvc._
import play.api.mvc.Results._
import jp.t2v.lab.play2.auth._
import scala.reflect.{ClassTag, classTag}
import model.{User, Permission}
import play.Logger
import scala.concurrent.{ExecutionContext, Future}
import play.api.cache.Cache

object Helper {
  def encodeBasicAuth[A](username: String, password: String): String = {
    val base64 = new String(org.apache.commons.codec.binary.Base64.encodeBase64(s"$username:$password".getBytes))
    s"Basic $base64"
  }

  def findBasicAuth[A](implicit request: Request[A]): Option[(String, String)] = {
    request.headers.get("Authorization").flatMap {
      authorization =>
        val drop = authorization.split(" ").drop(1)
        drop.headOption.flatMap {
          encoded =>
            val decoded = new String(org.apache.commons.codec.binary.Base64.decodeBase64(encoded.getBytes))
            decoded.split(":").toList match {
              case u :: p :: Nil => Some((u, p))
              case _ => None
            }
        }
    }
  }
}


trait AuthConfigImpl extends AuthConfig {

  type Id = String

  type User = model.User

  type Authority = Permission

  val idTag: ClassTag[Id] = classTag[Id]

  val sessionTimeoutInSeconds: Int = 3600

  def resolveUser(id: Id): Option[User] = throw new AssertionError("dont use!")

  override def resolveUserAsync(id: Id)(implicit context: ExecutionContext): Future[Option[User]] =
    User.findUserByNameAsync(id)

  def logoutSucceeded(request: RequestHeader): Result = Redirect(routes.Application.index)

  def authenticationFailed(request: RequestHeader): Result = {
    request.headers.get("X-Requested-With") match {
      case Some("XMLHttpRequest") => Unauthorized("Authentication failed")
      case _ =>
        Redirect(routes.Application.login).withSession("access_uri" -> request.uri)
    }
  }

  def loginSucceeded(request: RequestHeader): Result = {
    val uri = request.session.get("access_uri").getOrElse(routes.Main.index.url.toString)
    Redirect(uri).withSession(request.session - "access_uri")
  }


  def authorizationFailed(request: RequestHeader): Result = Forbidden("no permission")

  def authorize(user: User, authority: Authority): Boolean = {
    // FIXME abj impl this
    Logger.info(s"authorize $user as $authority -> true")
    true
  }

  // TODO abj auth is not stored at all, when this is set to true...
  override lazy val cookieSecureOption: Boolean = play.api.Play.current.configuration.getBoolean("auth.cookie.secure").getOrElse(false)

}

case class AuthenticatedRequest[A](val user: User, request: Request[A]) extends WrappedRequest(request)


trait Security {

  import play.api.Play.current

  val unAuthorized = Forbidden("You are not authorized") // unauthorized would be more correct, but triggers a browser loginbox... .withHeaders("WWW-Authenticate" -> """Basic realm="Secured"""")

  def Authenticated(f: AuthenticatedRequest[AnyContent] => Result): Action[AnyContent] = {
    AuthenticatedP(play.api.mvc.BodyParsers.parse.anyContent)(f)
  }

  def AuthenticatedP[A](p: BodyParser[A])(f: AuthenticatedRequest[A] => Result): Action[A] = {
    Action(p) {
      implicit request =>
        Helper.findBasicAuth(request).flatMap {
          case (u, p) =>
            Logger.debug("user trying to log in " + u)
            Cache.getOrElse[Option[User]](u, 6000) {
              //              inTransaction {
              User.authenticate(u, p)
              //              }
            }
        }.map {
          user => f(AuthenticatedRequest(user, request))
        }.getOrElse(unAuthorized)
    }
  }


  /*
    def AuthenticatedJson[A](f: AuthenticatedRequest[A] => AnyRef): Action[AnyRef] = {
      AuthenticatedJsonP(play.api.mvc.BodyParsers.parse.anyContent)(f)
    }

    def AuthenticatedJsonP[A](p: BodyParser[A])(f: AuthenticatedRequest[A] => AnyRef): Action[AnyRef] = {
      AuthenticatedP(p) {
        implicit request =>
          inTransaction {
            val res = f(request)
            val json: String = com.codahale.jerkson.Json.generate(res)
            val jsValue = play.api.libs.json.Json.parse(json)
            Results.Ok(jsValue)
          }

      }

    }
  */

}



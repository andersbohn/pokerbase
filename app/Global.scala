
import controllers.AuthConfigImpl
import jp.t2v.lab.play2.auth.OptionalAuthElement
import play.api.mvc._
import scala.concurrent.Future


/*
object AuthCookieFilter extends Filter with Controller with AuthConfigImpl with OptionalAuthElement {

  import scala.concurrent.ExecutionContext.Implicits.global

  def apply(nextFilter: RequestHeader => Future[SimpleResult])(requestHeader: RequestHeader): Future[SimpleResult] = {
    nextFilter(requestHeader).map {
      req =>
        val maybeUser: Option[User] = loggedIn(req)
        if (maybeUser.isDefined) {
          req.withCookies(Cookie("user", s"""{'username': abj, 'role': role }"""))
        } else {
          req
        }


    }
  }

  def apply(nextFilter: (RequestHeader) => Result)(requestHeader: RequestHeader): Result = {
    // FIXME abj conditional on authenticated user and:  insert real username + role
    nextFilter(requestHeader).withCookies(Cookie("user", s"""{'username': abj, 'role': role }"""))

  }

}

*/

/*
object AccessLog extends Filter {



//  def apply(f : scala.Function1[RequestHeader, Result])(rh : RequestHeader) : Result = {
//
//  }

//  def apply(f: (RequestHeader) => Result)(rh: RequestHeader): Result = {}

  def apply(next: RequestHeader => Future[SimpleResult])(request: RequestHeader): Future[SimpleResult] = {
    val result = next(request)
    result.map { r =>
      r.withCookies(Cookie("user", s"""{'username': abj, 'role': role }"""));
      r
    }
  }
}
*/

//object Global extends WithFilters(AuthCookieFilter) {
//
//}

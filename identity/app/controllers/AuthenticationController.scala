package controllers

import common.ImplicitControllerExecutionContext
import form.Mappings
import idapiclient.responses.{CookieResponse, CookiesResponse}
import idapiclient.{EmailPassword, IdApiClient, TrackingData}
import model.{ApplicationContext, NoCache}
import org.joda.time.DateTime
import play.api.http.HttpConfiguration
import play.api.libs.json.{Format, JodaWrites, Json, Writes}
import play.api.mvc._
import services._
import utils.SafeLogging

trait IdentityJsonProtocol {

  implicit val dtFormat = Format[DateTime](play.api.libs.json.JodaReads.DefaultJodaDateTimeReads, play.api.libs.json.JodaWrites.JodaDateTimeWrites)
  implicit val cookiesFormat = Json.format[CookieResponse]
  implicit val cookieFormat = Json.format[CookiesResponse]
}

class AuthenticationController(
  api : IdApiClient,
  idRequestParser: IdRequestParser,
  idUrlBuilder: IdentityUrlBuilder,
  authenticationService: AuthenticationService,
  signInService : PlaySigninService,
  val controllerComponents: ControllerComponents,
  val httpConfiguration: HttpConfiguration
)(implicit context: ApplicationContext)
  extends BaseController with ImplicitControllerExecutionContext with SafeLogging with Mappings with implicits.Forms with IdentityJsonProtocol {

  def authenticateUsernamePassword(): Action[AnyContent] = Action.async { implicit request =>
    val authResponse = api.authBrowser(EmailPassword("rahil.bohra.freelance@guardian.co.uk", "password123", None), TrackingData(None, None, None, None, None, None))
    authResponse.map {
      case Right(cookies) =>
        NoCache(Ok(Json.toJson(cookies)))
      case Left(errors) =>
        NoCache(InternalServerError(Json.toJson(errors)))
    }
  }
}

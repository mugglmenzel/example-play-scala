package controllers

import models.Dog
import play.api.libs.iteratee.Enumerator
import play.api.libs.json._
import play.api.mvc.{Action, Controller}
import play.api.libs.functional.syntax._
import scala.concurrent.ExecutionContext.Implicits.global

/**
 * Created by d046179 on 9/23/15.
 */
object RestController extends Controller {

  implicit val dogWrite: Writes[Dog] = (
    (JsPath \ "id").write[String] and
      (JsPath \ "name").write[String]
  )(unlift(Dog.unapply))

  implicit val dogRead: Reads[Dog] = (
    (JsPath \ "id").read[String] and
      (JsPath \ "name").read[String]
    )(Dog.apply _)


  def index = Action {implicit request =>
    render {
      case Accepts.Html() => Ok(views.html.index("Rest Demo"))
      case Accepts.Json() => Ok(Json.obj("message" -> "Welcome to /rest!"))
    }
  }

  def createDog = Action(parse.json) { implicit request =>
    println(request.body)
    val parsedDog = request.body.validate[Dog]
    parsedDog.foreach(println)
    parsedDog.fold(error => BadRequest, dog => Ok)
  }

  def readDog = Action {
    Ok(Json.toJson(new Dog("dog-1", "doggie"))).as(JSON)
  }

  def readListChunked(list: String) = Action { implicit request =>
    Ok.chunked(Enumerator("a", "b", "c").map(list + ":" + _ + "\n"))
  }

}

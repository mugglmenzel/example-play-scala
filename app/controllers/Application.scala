package controllers

import play.api._
import play.api.cache.Cache
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc._

import play.api.Play.current

case class MessageData(name: String, message: String)

object Application extends Controller {

  def index = Action {
    Cache.getOrElse("index", 15*60)(Ok(views.html.index("Your new application is ready.")))
  }

  def sendForm = Action { implicit request =>
    Form(tuple("name" -> text, "message" -> text)).bindFromRequest.fold(error => BadRequest, success =>
      Ok(views.html.success(success._1, success._2))
    )
  }

  def sendFormObj = Action { implicit request =>
    println(request.body)
    Form(mapping("name" -> text, "message" -> text)(MessageData.apply)(MessageData.unapply)).bindFromRequest.fold(error => BadRequest, success =>
      Ok("Got " + success)
    )
  }

}

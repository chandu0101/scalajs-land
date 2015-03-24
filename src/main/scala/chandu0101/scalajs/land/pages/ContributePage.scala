package chandu0101.scalajs.land.pages

import chandu0101.scalajs.facades.pouchdb.{PouchDB, PouchDBException}
import chandu0101.scalajs.land.util.Constants._
import chandu0101.scalajs.react.components.optionselectors.DefaultSelect
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.all._

import scala.async.Async.{async, await}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.scalajs.js.Dynamic.{literal => json}
import scala.scalajs.js.{Date, JSON, RegExp}


/**
 * Created by chandrasekharkode on 3/14/15.
 */
object ContributePage {


  case class State(url : String = "",title : String = "",description : String = "",category : String = "Tutorial/BlogPost", status: String = "" ,error : String = "")

  class Backend(t: BackendScope[_, State]) {

    val db = PouchDB.create("https://mmoredingencencedinglese:lDfseNlifUCP4f8gFWuE5xrE@chandu0101.cloudant.com/scalajs-land-temp")

    def isValidURl(url : String) =  {
      val regex = new RegExp("^(http|https|ftp)\\://([a-zA-Z0-9\\.\\-]+(\\:[a-zA-Z0-9\\.&amp;%\\$\\-]+)*@)*((25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9])\\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[0-9])|([a-zA-Z0-9\\-]+\\.)*[a-zA-Z0-9\\-]+\\.(com|edu|gov|int|mil|net|org|biz|arpa|info|name|pro|aero|coop|museum|[a-zA-Z]{2}))(\\:[0-9]+)*(/($|[a-zA-Z0-9\\.\\,\\?\\'\\\\\\+&amp;%\\$#\\=~_\\-]+))*$")
      regex.test(url)
    }

    def handleURL(e: ReactEventI) = {
      t.modState(_.copy(url = e.target.value))
    }

    def handleTitle(e: ReactEventI) = {
       t.modState(_.copy(title = e.target.value))
    }

    def handleDescription(e: ReactEventI) = {
      t.modState(_.copy(description = e.target.value))
    }

    def handleCategory(value : String) = {
      t.modState(_.copy(category = value))
    }

    def saveDoc = async {
      val id = new Date().toJSON()
      val s = t.state
      await(db.put(json(TITLE -> s.title,DESCRIPTION -> s.description,CATEGORY -> s.category,URL -> s.url),id))
      t.modState(s => State(status = s"Successfully Saved ${s.title} - ${s.category} ! ,It'll be on the web once its approved, thank you for your contribution :) "))
    }.recover {
      case ex => t.modState(_.copy(error = s"Error saving doc .. ${JSON.stringify(ex.asInstanceOf[PouchDBException].err)}" ,status = ""))
    }
    
    def handleSubmit = {
      var error = ""
      if(!isValidURl(t.state.url)) error = s"$error Enter Valid URL .. # " 
      if(t.state.title.isEmpty) error = s"$error Enter Valid Title  # "
      val desc = t.state.description
      if(desc.isEmpty || desc.length < 50 || desc.length > 800) error = s"$error Enter short description with minimum of 50 chars and max of 800 chars  # "
      if(error.nonEmpty) t.modState(_.copy(error = error,status = ""))
      else {
        saveDoc
      }
    }

  }

  val component = ReactComponentB[Unit]("Contribute")
    .initialState(State())
    .backend(new Backend(_))
    .render((P,S,B) => {
    div(cls := "contribute",
     S.error.nonEmpty ?= div(cls := "error" ,ul(S.error.split("#").map(s => li(s)))),
     S.status.nonEmpty ?= div(cls := "status" ,S.status),
     div(cls := "form",
       div(input(tpe := "text",value := S.url ,onChange ==> B.handleURL ,placeholder := "Enter URL ..")) ,
       div(input(tpe := "text",value := S.title ,onChange ==> B.handleTitle ,placeholder := "Enter Title ..")) ,
       div(textarea(tpe := "text",value := S.description ,onChange ==> B.handleDescription ,placeholder := "Enter Short Description .." )),
       div(DefaultSelect(label = "Category :" ,options = List(TUTORIAL,BOOK,VIDEO,LIBRARY),onChange = B.handleCategory ,value = S.category)),
       div(a("Submit" ,onClick --> B.handleSubmit))
     )
    )
  }).buildU

  def apply() = component()
}

package chandu0101.scalajs.land.pages


import chandu0101.scalajs.land.routes.AppRouter.AppPage
import chandu0101.scalajs.land.routes.AppRouter.AppPage._
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.all._


/**
 * Created by chandrasekharkode on 3/18/15.
 */
object Header {


  val component = ReactComponentB[Unit]("Header")
    .render((P) => {
    header( cls := "header",
        nav(
          a("Home" ,href := "#", currentRoute.equals("") ?= (cls := "active")),
          a("Libraries" ,href := libraries.path.value , currentRoute.equals(libraries.path.value) ?= (cls := "active") ),
          a("Tutorials" ,href := tutorials.path.value, currentRoute.equals(tutorials.path.value) ?= (cls := "active")),
          a("Videos" ,href := videos.path.value, currentRoute.equals(videos.path.value) ?= (cls := "active")),
          a("Books" ,href := books.path.value, currentRoute.equals(books.path.value) ?= (cls := "active")),
          a("Contribute" ,href := contribute.path.value, currentRoute.equals(contribute.path.value) ?= (cls := "active"))
        )
       )
    }).buildU

  def apply() = component()
}

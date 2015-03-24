package chandu0101.scalajs.land.pages

import chandu0101.scalajs.land.components.{CategoryPageTemplate, ScalaJSRepos, TodoApp}
import chandu0101.scalajs.land.util.Constants
import chandu0101.scalajs.land.util.Constants._
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.all._


/**
 * Created by chandrasekharkode on 2/25/15.
 */
object HomePage {

  val component = ReactComponentB[Unit]("HomePage")
    .render(P => {
    div(cls := "home")(
     div(cls := "info")(
      p("If you don't know what scala-js is then please check " , a(href := "http://www.scala-js.org/", target := "_blank" , " here ") ," ,still reading! welcome to scalajs-land :) ")
     ),
     div(cls := "why")(
      div(cls := "title")(h3("Why scala-js :")),
      ul(
       li("Libraries instead of primitives"),
       li("Flexible type system"),
       li("Share code between client and server"),
       li("Strong typing, including for JavaScript libraries"),
       li("Tooling: IDE support, sbt integration, dependency, unit testing, stack traces and source maps, cross-compiling projects"),
       li("Portability: bring Scala to environments powered by JavaScript, most importantly the browsers, but also others, such as the brand new React Native"),
       li("Integration with JavaScript"),
       li("Fast compilation cycle (wrt other compile-to-JS options such as GWT)"),
       li("Statically-checked client-server RPC with Autowire"),
       li("Very good documentation")
      )
     ),
     div(cls := "awesome")(
      blockquote("Why does Scala-JS work so well ? because of ",a(href := "https://twitter.com/sjrdoeraene",target := "_blank" ,"@sjrd")," and the great people who contribute - " ,a(href := "https://twitter.com/odersky" ,target := "_blank" ,"Martin Odersky"))
     )
    )
  }).buildU

  def apply() = component()

}

package chandu0101.scalajs.land.pages


import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.all._


/**
 * Created by chandrasekharkode on 3/18/15.
 */
object Footer {


  val component = ReactComponentB[Unit]("Footer")
    .render(P => {
       footer(
        p("Built using scala-js")
       )
    }).buildU

  def apply() = component()
}

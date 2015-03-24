package chandu0101.scalajs.land.pages


import chandu0101.scalajs.land.components.CategoryPageTemplate
import chandu0101.scalajs.land.util.Constants._
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.all._


/**
 * Created by chandrasekharkode on 3/18/15.
 */
object LibrariesPage {

  val component = ReactComponentB[Unit]("LibrariesPage")
    .render(P => {
      CategoryPageTemplate(category = LIBRARY)
    }).buildU

  def apply() = component()
}

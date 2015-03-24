package chandu0101.scalajs.land.pages

import chandu0101.scalajs.land.components.CategoryPageTemplate
import chandu0101.scalajs.land.util.Constants._
import japgolly.scalajs.react._


/**
  * Created by chandrasekharkode on 3/18/15.
  */
object TutorialsPage {

   val component = ReactComponentB[Unit]("TutorialsPage")
     .render(P => {
       CategoryPageTemplate(category = TUTORIAL)
     }).buildU

   def apply() = component()
 }

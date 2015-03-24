package chandu0101.scalajs.land.routes

import chandu0101.scalajs.land.components.TodoApp
import chandu0101.scalajs.land.pages._
import japgolly.scalajs.react._
import japgolly.scalajs.react.extra.router.{AbsUrl, BaseUrl, Redirect, RoutingRules}
import japgolly.scalajs.react.vdom.all._
import org.scalajs.dom


/**
 * Created by chandrasekharkode on 2/25/15.
 */
object AppRouter {


  object AppPage extends RoutingRules {

    val root = register(rootLocation(HomePage()))

    val tutorials : Loc = register(location("#tutorials",TutorialsPage()))

    val videos : Loc = register(location("#videos",VideosPagePage()))

    val books : Loc = register(location("#books",BooksPage()))

    val libraries : Loc = register(location("#libraries",LibrariesPage()))
    val contribute : Loc = register(location("#contribute",ContributePage()))

    register(removeTrailingSlashes)

    override protected val notFound = redirect(root, Redirect.Replace)

    def currentRoute = dom.window.location.href.substring(baseUrl.value.length)

    override protected def interceptRender(i: InterceptionR): ReactElement =
       div(
         Header(),
         i.element,
         Footer()
       )
  }

  val baseUrl = BaseUrl.fromWindowOrigin / "scalajs-land/"
//  val baseUrl = BaseUrl("https://chandu0101.github.io/scalajs-land/")

  val C = AppPage.router(baseUrl)
}

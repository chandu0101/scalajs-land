package chandu0101.scalajs.land.components


import chandu0101.scalajs.facades.pouchdb._
import chandu0101.scalajs.land.util.Constants._
import chandu0101.scalajs.react.components.optionselectors.DefaultSelect
import chandu0101.scalajs.react.components.pagers.Pager
import japgolly.scalajs.react._
import japgolly.scalajs.react.extra.OnUnmount
import japgolly.scalajs.react.vdom.all._

import scala.async.Async.{async, await}
import scala.collection.immutable
import scala.concurrent.ExecutionContext.Implicits.global
import scala.scalajs.js
import scala.scalajs.js.{Any, Array => JArray, JSON, UndefOr}

/**
 * Created by chandrasekharkode on 3/16/15.
 */
object CategoryPageTemplate {


  case class State(limit: Int, skip: Int = 0, rows: JArray[js.Dynamic] = JArray(), totalRows: Int = 0, error: String = "", filterText: String = "")

  class Backend(t: BackendScope[Props, State]) extends OnUnmount{

    val db = PouchDB.create(t.props.localDB)

    var changes : ChangesEventEmitter = null

    def getNewData = {
        changes = db.replicate.from(t.props.remoteDB).onComplete((resp: js.Dynamic) => {
        if (resp.docs_written.asInstanceOf[Int] > 0) updateTable(t.state.skip, t.state.limit)
      })
        .onError((err: js.Dynamic) => println(s"Error replicating from remoteDB ${t.props.remoteDB}  $err"))
    }

    def updateTable(newSkip: Int, newLimit: Int ,search : String = "") = async {

      var opts: QuickSearchOptions = null
      val filter : js.Function1[js.Dynamic,Boolean] = (doc : js.Dynamic) => {
        doc.category.toString == t.props.category
      }
      if (search.isEmpty)
        opts = QuickSearchOptions.query(t.props.category).fields(JArray(CATEGORY)).skip(newSkip).limit(newLimit).include_docs(true).result
      else
        opts = QuickSearchOptions.query(search).fields(JArray(TITLE, DESCRIPTION)).skip(newSkip).limit(newLimit).mm(1).include_docs(true).filter(filter).result
      val resp = await(db.search(opts))
      val rows = resp.rows.asInstanceOf[JArray[js.Dynamic]]
      t.modState(_.copy(rows = rows, totalRows = resp.total_rows.asInstanceOf[Int], limit = newLimit, skip = newSkip ,filterText = search))

    }.recover {
      case ex => t.modState(_.copy(error = s"Error getting docs .. ${JSON.stringify(ex.asInstanceOf[PouchDBException].err)}"))
    }

    def handleNextClick(e: ReactEventH): Unit = {
      val newOffset = t.state.skip + t.state.limit
      updateTable(newOffset, t.state.limit,t.state.filterText)
    }

    def handlePreviousClick(e: ReactEventH): Unit = {
      val newOffset = t.state.skip - t.state.limit
      updateTable(newOffset, t.state.limit,t.state.filterText)
    }

    def handlePageSizeChange(newSize: String): Unit = {
      updateTable(0, newSize.toInt)
    }

    def handleSearchText(e: ReactEventI): Unit = {
       updateTable(0,t.props.pageSize,e.target.value.trim)
    }

    /**
     * clean up
     */
    onUnmount(() => {
      if(changes != null) changes.cancel()
    })

  }

  val searchBar = ReactComponentB[Backend]("SearchBar")
    .render(P => {
    div( cls := "searchBar",
      input(tpe := "text", onChange ==> P.handleSearchText ,placeholder := "Search ...")
    )
  }).build

  val settingsBar = ReactComponentB[(Props, Backend, State)]("settingbar")
    .render(P => {
    val (p, b, s) = P
    var value = ""
    var options: List[String] = Nil
    val total = s.totalRows
    if (total > p.pageSize) {
      value = s.limit.toString
      options = immutable.Range.inclusive(p.pageSize, total, 10 * (total / 100 + 1)).:+(total).toList.map(_.toString)
    }
    div(cls := "settingsBar")(
      span(float := "right")(strong("Total : " + s.totalRows)),
      options.nonEmpty ?= DefaultSelect(label = "Page Size : ", options = options, value = value, onChange = b.handlePageSizeChange)
    )
  }).build

  val pageData = ReactComponentB[(Props, State, Backend)]("pageData")
    .render(P => {
    val (p, s, b) = P
    div( cls := "content",
      searchBar(b),
      settingsBar((p,b,s)),
      if(s.rows.nonEmpty) div( cls := "items",
        s.rows.zipWithIndex.map {
        case (row, index) => a(cls := "item" ,key := index ,target := "_blank" ,href := row.doc.url.toString)(
           h3(row.doc.title.toString),
           div(row.doc.description.toString)
        )
      }) else strong("Loading Results Please wait ..."),
      Pager(itemsPerPage = s.limit, totalItems = s.totalRows, offset = s.skip, nextClick = b.handleNextClick, previousClick = b.handlePreviousClick)
    )
  }).build


  val component = ReactComponentB[Props]("CategoryPage")
    .initialStateP(p => State(limit = p.pageSize))
    .backend(new Backend(_))
    .render((P, S, B) => {
    div( cls := "categoryPage",
      if (S.error.nonEmpty) strong(S.error)
      else pageData((P,S,B))
    )
  })
    .componentWillMount(scope => scope.backend.updateTable(scope.state.skip, scope.state.limit))
    .componentDidMount(scope => scope.backend.getNewData)
    .build

  case class Props(localDB: String, remoteDB: String,pageSize: Int, category: String)

  def apply(ref: UndefOr[String] = "", key: Any = {}, localDB: String = "scalajs-land", remoteDB: String = "https://hicerteedgetokerepection:rGYlUDLGC2JCb1EjC7qoYUi8@chandu0101.cloudant.com/scalajs-land", pageSize: Int = 5, category: String) =
    component.set(key, ref)(Props(localDB, remoteDB, pageSize,category))
}

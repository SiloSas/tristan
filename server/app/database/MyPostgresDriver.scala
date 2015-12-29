package database

import com.github.tminglei.slickpg._

trait MyPostgresDriver extends ExPostgresDriver
with PgArraySupport
with PgDate2Support
with PgPlayJsonSupport
with PgNetSupport
with PgLTreeSupport
with PgRangeSupport
with PgHStoreSupport
with PgSearchSupport {

  override val pgjson = "jsonb"
  override lazy val Implicit = new ImplicitsPlus {}
  override val simple = new SimpleQLPlus {}

  trait ImplicitsPlus extends Implicits
  with ArrayImplicits
  with DateTimeImplicits
  with RangeImplicits
  with HStoreImplicits
  with JsonImplicits
  with SearchImplicits

  trait SimpleQLPlus extends SimpleQL
  with ImplicitsPlus
  with SearchAssistants
  ///
  override val api = new API with ArrayImplicits
    with DateTimeImplicits
    with PlayJsonImplicits
    with NetImplicits
    with LTreeImplicits
    with RangeImplicits
    with HStoreImplicits
    with SearchImplicits
    with SearchAssistants {}
}

object MyPostgresDriver extends MyPostgresDriver
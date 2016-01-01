package Shared

import java.sql.Date

case class Project(id: String, name: String, description: String, image: String, tags: Seq[String], technologies: Seq[String], date: String,
                   isLandscape: Boolean, maxWidth: Int, maxHeight: Int, columnNumber: Int)



case class Technology(technologies: String)
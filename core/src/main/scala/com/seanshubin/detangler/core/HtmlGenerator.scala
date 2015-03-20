package com.seanshubin.detangler.core

import java.io.{PrintWriter, Writer}

class HtmlGenerator(out: PrintWriter, detangled: Detangled) {
  def generateIndex(infos: Seq[UnitInfo], arrows: Seq[Arrow]): Unit = {
    indexHeader()
    infos.foreach(indexInfo)
    //    arrows.foreach(indexArrow(out, _))
    indexFooter()
  }

  private def indexHeader(): Unit = {
    emitHtmlHeader("dependency report")
  }

  private def indexFooter(): Unit = {
    emitHtmlFooter()
  }

  private def indexInfo(info: UnitInfo): Unit = {
    emitTableHeader(info.id.qualifiedName, Seq(
      Seq("name", "depth", "complexity", "composed of"),
      Seq(info.id.name, info.depth.toString, info.complexity.toString, info.id.fileSystemName)))
    emitTableFooter()
    out.println("<ul>")
    indexInfoDependency("depends on", info.dependsOn)
    indexInfoDependency("depended on by", info.dependedOnBy)
    out.println("</ul>")
  }

  def indexInfoDependency(caption: String, dependencies: Set[UnitId]): Unit = {
    val part1 =
      s"""    <li>
         |       <table>
         |           <thead>
         |           <tr>
         |               <th colspan="4">""".stripMargin
    val part2 =
      s"""           </tr>
         |           <tr>
         |               <th>name</th>
         |               <th>depth</th>
         |               <th>complexity</th>
         |               <th>composed of</th>
         |           </tr>
         |           </thead>
         |           <tbody>""".stripMargin
    val part3 =
      """|            </tbody>
        |        </table>
        |    </li>""".stripMargin
    out.write(part1)
    out.println(s"$caption (${dependencies.size})</th>")
    out.println(part2)
    dependencies.foreach(indexInfoDependencyEntry)
    out.println(part3)
  }

  def indexInfoDependencyEntry(unitId: UnitId): Unit = {
    val unitInfo = detangled.map(unitId)
    emitDataRow(Seq(
      unitId.name,
      unitInfo.depth.toString,
      unitInfo.complexity.toString,
      unitId.fileSystemName))
  }

  private def emitHtmlHeader(title: String): Unit = {
    val part1 =
      """<!DOCTYPE html>
        |<html>
        |<head>
        |    <meta charset="UTF-8">
        |    <title>""".stripMargin
    val part2 =
      """</title>
        |    <style>
        |        table {
        |            border-collapse: collapse;
        |            margin-bottom: 2em;
        |        }
        |        table, th, td {
        |            border: 1px solid black;
        |            padding: 5px;
        |        }
        |        ul {
        |            list-style: none;
        |        }
        |        li {
        |            margin-left: 0;
        |        }
        |    </style>
        |</head>
        |<body>""".stripMargin
    out.write(part1)
    out.write(title)
    out.println(part2)
  }

  private def emitTableFooter(): Unit = {
    val part1 =
      """    </tbody>
        |</table>""".stripMargin
    out.println(part1)
  }

  private def emitTableHeader(id: String, headerRows: Seq[Seq[String]]): Unit = {
    val part1 = """<table id="""".stripMargin
    val part2 =
      """">
        |    <thead>""".stripMargin
    val part3 =
      """    </thead>
        |    <tbody>""".stripMargin
    out.write(part1)
    out.write(id)
    out.println(part2)
    emitHeaderRows(headerRows)
    out.println(part3)
  }

  private def emitHtmlFooter(): Unit = {
    val text =
      """</body>
        |</html>""".stripMargin
    out.println(text)
  }

  private def emitHeaderRows(headerRows: Seq[Seq[String]]): Unit = {
    headerRows.foreach(emitHeaderRow)
  }

  private def emitHeaderRow(headerRow: Seq[String]): Unit = {
    out.println("        <tr>")
    headerRow.foreach(emitHeaderCell)
    out.println("        </tr>")
  }

  private def emitDataRow(row: Seq[String]): Unit = {
    out.println("        <tr>")
    row.foreach(emitDataCell)
    out.println("        </tr>")
  }

  private def emitHeaderCell(headerCell: String): Unit = {
    out.write("            <th>")
    out.write(headerCell)
    out.println("</th>")
  }

  private def emitDataCell(headerCell: String): Unit = {
    out.write("            <td>")
    out.write(headerCell)
    out.println("</td>")
  }
}

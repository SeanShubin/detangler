package com.seanshubin.detangler.core

import java.io.Writer

class HtmlGenerator(out:Writer, detangled:Detangled) {
  def generateIndex(infos: Seq[UnitInfo], arrows: Seq[Arrow]): Unit = {
    indexHeader()
    infos.foreach(indexInfo)
//    arrows.foreach(indexArrow(out, _))
    indexFooter()
  }

  private def indexHeader(): Unit = {
    val text =
      """<!DOCTYPE html>
        |<html>
        |<head>
        |    <meta charset="UTF-8">
        |    <title>dependency report</title>
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
    out.write(text)
  }

  private def indexFooter(): Unit = {
    val text =
      """</body>
        |</html>""".stripMargin
    out.write(text)
  }

  private def indexInfo(info:UnitInfo):Unit = {
    val header =
      s"""<table id="${info.id.qualifiedName}">
      |    <thead>
      |    <tr>
      |        <th>name</th>
      |        <th>depth</th>
      |        <th>complexity</th>
      |        <th>composed of</th>
      |    </tr>
      |    <tr>
      |        <td>${info.id.name}</td>
      |        <td>${info.depth}</td>
      |        <td>${info.complexity}</td>
      |        <td><a href="${info.id.fileSystemName}">parts</a></td>
      |    </tr>
      |    </thead>
      |    <tbody>
      |    </tbody>
      |</table>
      |<ul>""".stripMargin
    val footer = "</ul>"
    out.write(header)
    indexInfoDependency("depends on", info.dependsOn)
    indexInfoDependency("depended on by", info.dependedOnBy)
    out.write(footer)
  }

  def indexInfoDependency(caption:String, dependencies: Set[UnitId]): Unit = {
     val header =
       s"""    <li>
         |        <table>
         |            <thead>
         |            <tr>
         |                <th colspan="4">${caption} (${dependencies.size})</th>
         |            </tr>
         |            <tr>
         |                <th>name</th>
         |                <th>depth</th>
         |                <th>complexity</th>
         |                <th>composed of</th>
         |            </tr>
         |            </thead>
         |            <tbody>""".stripMargin
    val footer =
      """|            </tbody>
         |        </table>
         |    </li>""".stripMargin
    out.write(header)
    dependencies.foreach(indexInfoDependencyEntry)
    out.write(footer)
  }

  def indexInfoDependencyEntry(unitId:UnitId):Unit = {
    val unitInfo = detangled.map(unitId)
    val body =
    s"""<tr>
      |  <td>${unitId.name}</td>
      |  <td>${unitInfo.depth}</td>
      |  <td>${unitInfo.complexity}</td>
      |  <td><a href="${unitId.fileSystemName}">parts</a></td>
      |</tr>
    """.stripMargin
    out.write(body)
  }
}

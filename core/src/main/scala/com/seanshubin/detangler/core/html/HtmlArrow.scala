package com.seanshubin.detangler.core.html

case class HtmlArrow(id:String, from:HtmlAnchor, to:HtmlAnchor, parts:Seq[HtmlArrow])

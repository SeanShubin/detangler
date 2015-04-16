package com.seanshubin.detangler.core

import org.jsoup.Jsoup
import org.jsoup.nodes.{Document, Element}

import scala.collection.JavaConversions

class JsoupApiImpl extends JsoupApi {
  override def parse(html: String): JsoupFragment = {
    val document: Document = Jsoup.parse(html, "")
    val fragment: JsoupFragment = new JsoupFragmentImpl(document)
    fragment
  }

  override def concat(left: JsoupFragment, right: JsoupFragment): JsoupFragment = {
    val newDocument = doc(left)
    children(right).foreach(newDocument.body().appendChild(_))
    new JsoupFragmentImpl(newDocument)
  }

  private def doc(x: JsoupFragment): Document = {
    x.asInstanceOf[JsoupFragmentImpl].document
  }

  private def children(x: JsoupFragment): Seq[Element] = {
    val document = doc(x)
    JavaConversions.collectionAsScalaIterable(document.body().children()).toSeq
  }

  private class JsoupFragmentImpl(originalDocument: Document) extends JsoupFragment {
    def document: Document = originalDocument.clone()

    override def asText: String = originalDocument.body().html()
  }

}

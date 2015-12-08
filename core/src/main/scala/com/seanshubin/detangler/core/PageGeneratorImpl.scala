package com.seanshubin.detangler.core

class PageGeneratorImpl(detangled: Detangled,
                        resourceLoader: ResourceLoader) extends PageGenerator {
  override def pageForId(id: UnitId, template: HtmlFragment): String = {
    val pageTemplate = new PageTemplate(id, detangled, template)
    val fragment = pageTemplate.generate()
    fragment.toString
  }
}

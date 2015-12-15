package com.seanshubin.detangler.report

import com.seanshubin.detangler.model.{Detangled, Single}

class DependencyTemplateRulesImpl(detangled:Detangled, dependencyDirection:DependencyDirection) extends DependencyTemplateRules {
  override def generate(dependencyTemplate: HtmlElement,
                        context:Single,
                        single: Single): HtmlElement = {
    val dependencyRowTemplate = dependencyTemplate.select(".dependency-row")
    val childModules = dependencyDirection.dependenciesFor(detangled, context, single)
    if (childModules.isEmpty) {
      HtmlFragment.Empty
    } else {
      val rows = childModules.map(generateRow)
      val result = wrapperTemplate.
        text(".caption", s"${reasonDirection.caption} (${childModules.size})").
        appendAll(SelectorModuleDependencies, rows)
      result
    }


    """            <ul class="single-detail">
      |                <li>
      |                    <table>
      |                        <thead>
      |                        <tr>
      |                            <th class="caption" colspan="4">depends on (number)</th>
      |                        </tr>
      |                        <tr>
      |                            <th>name</th>
      |                            <th>depth</th>
      |                            <th>complexity</th>
      |                            <th>reason</th>
      |                        </tr>
      |                        </thead>
      |                        <tbody>
      |                        <tr>
      |                            <td><a class="name" href="">other/group</a></td>
      |                            <td class="depth">depth</td>
      |                            <td class="complexity">complexity</td>
      |                            <td><a class="reason" href="">reason</a></td>
      |                        </tr>
      |                        </tbody>
      |                    </table>
      |                </li>
      |            </ul>
      |
      |
    """.stripMargin
    HtmlElement.fragmentFromString("<p>SingleDetailTemplateRulesImpl not implemented</p>")
  }
}

package com.seanshubin.detangler.core

object TemplateUtil {
  def arrows(templateText: String, arrows: Seq[Arrow]): String = {
    """<ul> 
      |    <li id="group_a---group_b"> 
      |        <a class="from" href="#group_a">group/a</a> 
      |        <span>&rarr;</span> 
      |        <a class="to" href="#group_b">group/b</a> 
      |        <ul> 
      |            <li id="group_a--package_c---group_b--package_e"> 
      |                <a class="from" href="group_a.html#group_a--package_c">package/c</a> 
      |                <span>&rarr;</span>
      |                <a class="to" href="group_b.html#group_b--package_e">package/e</a> 
      |                <ul> 
      |                    <li id="group_a--package_c--class_f---group_b--package_e--class_i"> 
      |                        <a class="from" href="group_a--package_c.html#group_a--package_c--class_f">class/f</a>
      |                        <span>&rarr;</span> 
      |                        <a class="to" href="group_b--package_e.html#group_b--package_e--class_i">class/i</a> 
      |                    </li>
      |                </ul>
      |            </li>
      |        </ul>
      |    </li>
      |</ul>
      | """.stripMargin
  }
}

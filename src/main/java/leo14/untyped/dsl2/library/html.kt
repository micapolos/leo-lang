package leo14.untyped.dsl2.library

import leo14.untyped.dsl2.*

val html = library_ {
	render
	gives {
		function {
			recursive {
				anything.render
				does { text("") }

				quote { html { anything }.render }
				does {
					text("<html>")
					plus {
						given.html.content
						do_ { recurse }
					}
					plus { text("</html>") }
				}

				quote { div { anything }.render }
				does {
					text("<div>")
					plus {
						given.div.content
						do_ { recurse }
					}
					plus { text("</div>") }
				}

				given.content.render
			}
		}
	}

	anything.render
	does {
		given.previous.given.content.do_ { render }
	}

	html {
		div
	}.render.print
}

fun main() {
	html
}
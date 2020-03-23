package leo14.untyped.dsl2.library

import leo14.untyped.dsl2.*

val html = library_ {
	render
	gives {
		function {
			render
			gives { text("") }

			html.render
			does {
				text("<html>")
				plus {
					given.html.content
					do_ { recurse }
				}
				plus { text("</html>") }
			}

			given.content.render
		}
	}

	anything.render
	does {
		given.content.do_ { render }
	}

	html.render.print
}

fun main() {
	html
}
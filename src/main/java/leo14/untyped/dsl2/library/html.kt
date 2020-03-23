package leo14.untyped.dsl2.library

import leo14.untyped.dsl2.*

val html = library_ {
	anything.render
	does {
		given.previous.given.content
		do_ {
			function {
				recursive {
					text("")
					fold { given }
					doing {
						function {
							output.gives { given.text }

							tag { anything }.render
							does {
								output
								plus { text("<") }
								plus { given.tag.content.name }
								plus { text(">") }
								plus { given.tag.content.content.do_ { recurse } }
								plus { text("</") }
								plus { given.tag.content.name }
								plus { text(">") }
							}

							quote { tag { text }.render }
							does {
								output
								plus { given.tag.text }
							}

							tag { folded.content }.render
						}
					}
				}
			}
		}
	}

	html {
		div {
			text("Search engine")
		}
		div {
			text("Click ")
			a {
				text("Google")
			}
			text(" or ")
			a {
				text("Bing")
			}
			text(".")
		}
	}
	render.print
}

fun main() {
	html
}
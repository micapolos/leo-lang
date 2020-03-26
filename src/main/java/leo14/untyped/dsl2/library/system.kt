package leo14.untyped.dsl2.library

import leo14.untyped.dsl2.*

val system = library_ {
	text.say
	does {
		exec {
			text("say")
			it {
				text("\"")
				plus { given.text }
				plus { text("\"") }
			}
		}
		clear
	}
}

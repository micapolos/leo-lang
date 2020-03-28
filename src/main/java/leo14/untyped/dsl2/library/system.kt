package leo14.untyped.dsl2.library

import leo14.untyped.dsl2.*

val system = library_ {
	text.say
	does {
		exec {
			it { text("say") }
			it {
				text("\"")
				plus { given.text }
				plus { text("\"") }
			}
		}
		clear
	}

	text.html.open.does {
		text("leo14.js.compiler.MainKt").native.class_.static.invoke {
			it { text("htmlOpen") }
			it { given.text.native.string }
		}.clear
	}
}

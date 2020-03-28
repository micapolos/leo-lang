package leo14.untyped.dsl2.library

import leo14.untyped.dsl2.*

val system = library_ {
	text.say
	does {
		exec {
			it { text("say") }
			it {
				text("\"")
				plus { given.text.leo.text }
				plus { text("\"") }
			}
		}
		clear
	}
}

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

	text
	open { html }
	does {
		text("leo14.js.compiler.MainKt").class_.native.static.invoke {
			it { text("htmlOpen") }
			it { given.text.native { string } }
		}.clear
	}

	number.millis.sleep
	does {
		text("java.lang.Thread").class_.native.static.invoke {
			it { text("sleep") }
			it { given.sleep.millis.number.long.native }
		}
		clear
	}

	assert {
		number(1).millis.sleep.gives { nothing_ }
	}
}

fun main() = run_ { core(); system() }
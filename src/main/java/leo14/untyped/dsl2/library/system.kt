package leo14.untyped.dsl2.library

import leo14.untyped.dsl2.*

val system = library_ {
	text.say
	does {
		exec {
			it { text("say") }
			it {
				text("\"")
				plus { text }
				plus { text("\"") }
			}
		}
		clear
	}

	text
	open { html }
	gives {
		text("leo14.js.compiler.MainKt").class_.native.static.invoke {
			it { text("htmlOpen") }
			it { text.native { string } }
		}.clear
	}

	number.millis.sleep
	gives {
		text("java.lang.Thread").class_.native.static.invoke {
			it { text("sleep") }
			it { sleep.millis.number.long.native }
		}
		clear
	}

	assert {
		number(1).millis.sleep.gives { nothing_ }
	}
}

fun main() = run_ { core(); system() }
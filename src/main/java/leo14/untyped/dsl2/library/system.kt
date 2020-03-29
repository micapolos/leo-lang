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
		text("leo14.js.compiler.MainKt")
		native { class_ }
		invoke {
			static {
				it { text("htmlOpen") }
				it { given.text.native { string } }
			}
		}.clear
	}

	// TODO: Consider auto-making everything outside of "define".
	number.millis.does { given.object_.subject.make { millis } }
	assert { number(10).millis.gives { millis { number(10) } } }

	millis { number }
	sleep
	does {
		text("java.lang.Thread")
		native { class_ }
		invoke {
			static {
				it { text("sleep") }
				it { given.millis.number.native { long } }
			}
		}
		clear
	}

	assert {
		number(1).millis.sleep.gives { nothing_ }
	}
}

fun main() = run_ { core(); system() }
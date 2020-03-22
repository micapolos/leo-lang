package leo14.untyped.dsl2.example

import leo14.untyped.dsl2.*

fun main() {
	_run {
		text("javax.swing.JFrame")
		native { new }
		as_ { my.frame }

		my.frame
		invoke { text("show") }
		delete

		my.frame
		invoke {
			text("setSize")
			it { number(640).native { int } }
			it { number(480).native { int } }
		}
		delete
	}
}
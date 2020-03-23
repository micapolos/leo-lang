package leo14.untyped.dsl2.example

import leo14.untyped.dsl2.*

fun main() {
	_run {
		text("Checking...")

		assert {
			number(2)
			plus { number(2) }
			gives { number(4) }
		}

		plus { text(" OK") }
		print
	}
}
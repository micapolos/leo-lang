package leo14.untyped.dsl2.example

import leo14.untyped.dsl2.*

fun main() {
	_run {
		assert {
			number(2)
			plus { number(2) }
			gives { number(5) }
		}
	}
}
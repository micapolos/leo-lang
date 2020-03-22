package leo14.untyped.dsl2.example

import leo14.untyped.dsl2.*

fun main() {
	_run {
		text("Hello, world!")
		print

		text("Hello, ")
		plus { text("world!") }
		print

		number(2)
		plus { number(3) }
		print

		number(5)
		minus { number(3) }
		print

		number(2)
		times { number(3) }
		print

		minus { number(2) }
		print

		number(2)
		_equals { number(2) }
		print

		_if { number(2)._equals { number(2) } }
		_then { text("OK!") }
		_else { text("Oooops...") }
		print
	}
}
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
		equals_ { number(2) }
		print

		if_ { number(2).equals_ { number(2) } }
		then_ { text("OK!") }
		else_ { text("Oooops...") }
		print
	}
}
package leo14.untyped.dsl2.example

import leo14.untyped.dsl2.*

fun main() {
	run_ {
		number(10)
		do_ {
			function {
				given.number
				plus { number(1) }
			}
		}
		print

		function {
			given.number
			plus { number(1) }
		}
		apply { number(10) }
		print
	}
}
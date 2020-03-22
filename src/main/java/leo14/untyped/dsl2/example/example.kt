package leo14.untyped.dsl2.example

import leo14.untyped.dsl2.*

fun main() {
	_print {
		number.increment.gives {
			given.number
			plus { number(1) }
		}

		number.decrement.gives {
			given.number
			minus { number(1) }
		}

		my.point._is {
			point {
				x {
					number(10)
					plus { number(20).increment }
				}
				y {
					number(20).increment
					plus { number(14) }
				}
			}
		}

		my.point
	}
}
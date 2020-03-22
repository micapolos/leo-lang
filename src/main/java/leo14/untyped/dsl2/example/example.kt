package leo14.untyped.dsl2.example

import leo14.untyped.dsl2.*

fun main() {
	_print {
		number.increment
		does {
			given.number
			plus { number(1) }
		}

		number.decrement
		does {
			given.number
			minus { number(1) }
		}

		my.point
		gives {
			point {
				x {
					number(10)
					plus { number(20).increment }
				}
				y {
					number(20).decrement
					times { number(14) }
				}
			}
		}

		my.point
	}
}
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

		vector
		gives {
			vector {
				x { number }
				y { number }
			}
		}

		vector
		plus { vector }
		does {
			vector {
				x {
					given.vector.x.number
					plus { given.plus.vector.x.number }
				}
				y {
					given.vector.y.number
					plus { given.plus.vector.y.number }
				}
			}
		}

		vector {
			x { number(10).increment }
			y { number(20).decrement }
		}
		plus {
			vector {
				x {
					number(30)
					plus { number(40) }
				}
				y {
					number(40)
					times { number(2) }
				}
			}
		}
	}
}
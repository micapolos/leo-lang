package leo14.untyped.dsl2.example

import leo14.untyped.dsl2.*

fun main() {
	_run {
		number(0)
		fold {
			numbers {
				number(1)
				number(2)
				number(3)
				number(4)
				number(5)
			}
		}
		doing {
			function {
				given.number
				plus { folded.number }
			}
		}
		print
	}
}
package leo14.untyped.dsl2.example

import leo14.untyped.dsl2.*

fun main() {
	_run {
		quote {
			number(2)
			plus { number(3) }
			gives {
				unquote {
					number(2)
					plus { number(3) }
				}
			}
		}
		print
	}
}
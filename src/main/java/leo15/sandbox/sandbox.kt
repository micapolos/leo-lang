package leo15.sandbox

import leo14.untyped.dsl2.*

fun main() {
	main_ {
		point {
			x { number(10) }
			y { number(20) }
			z { number(30) }
		}
		x
		debug
	}
}

package leo15.sandbox

import leo15.dsl.*

fun main() {
	main_ {
		point {
			x { number(10) }
			y { number(20) }
			z { number(30) }
		}
		x.number.java
	}
}

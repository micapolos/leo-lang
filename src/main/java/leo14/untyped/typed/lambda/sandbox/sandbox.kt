package leo14.untyped.typed.lambda.sandbox

import leo14.untyped.dsl2.*

fun main() {
	main_ {
		point {
			x { number(10) }
			y { number(20) }
		}
		give {
			given.point.x.number
			plus { given.point.y.number }
		}
	}
}

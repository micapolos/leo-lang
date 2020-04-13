package leo14.untyped.typed.lambda.sandbox

import leo14.untyped.dsl2.*

fun main() {
	main_ {
		my.point.is_ {
			point {
				x { number(10) }
				y { number(20) }
			}
		}

		circle {
			radius { number(10) }
			center { my.point }
		}

//		my.circle
	}
}

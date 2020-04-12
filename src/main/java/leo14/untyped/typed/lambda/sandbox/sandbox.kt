package leo14.untyped.typed.lambda.sandbox

import leo14.untyped.dsl2.*

fun main() =
	main_ {
		circle {
			name { text("Moje kółeczko") }
			radius { number(30) }
			center {
				point {
					x { number(10) }
					y { number(20) }
				}
			}
		}
	}
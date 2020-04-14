package leo14.untyped.typed.lambda.sandbox

import leo14.untyped.dsl2.*

fun main() {
	main_ {
		boolean
		is_ {
			true_
			or { false_ }
		}

		boolean.not
		gives {
			given.not.negate
		}

		true_.not
	}
}

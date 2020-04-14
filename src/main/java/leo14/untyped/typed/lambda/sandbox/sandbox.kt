package leo14.untyped.typed.lambda.sandbox

import leo14.untyped.dsl2.*

fun main() {
	main_ {
		number.increment.gives {
			given.increment.number.plus { number(1) }
		}

		number.double.increment.gives {
			given.increment.double.number.increment.increment
		}

		number(123).double.increment
	}
}

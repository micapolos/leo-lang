package leo14.untyped.dsl.example

import leo14.untyped.dsl.*

val program =
	_program(
		number(), decrement(),
		gives(number(), minus(value(1))),

		recursive(
			number(), factorial(),
			gives(
				given(), number(),
				times(
					given(), number(), minus(value(1)), factorial())),

			value(1), factorial(),
			gives(value(1))))
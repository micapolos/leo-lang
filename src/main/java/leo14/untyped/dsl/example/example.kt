package leo14.untyped.dsl.example

import leo14.untyped.dsl.*

val program =
	_program(
		number(), decrement(),
		gives(number(), minus(number(1))),

		recursive(
			number(), factorial(),
			gives(
				given(), number(),
				times(
					given(), number(), minus(number(1)), factorial())),

			number(1), factorial(),
			gives(number(1))))
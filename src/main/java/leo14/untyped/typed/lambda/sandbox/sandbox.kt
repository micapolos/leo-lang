package leo14.untyped.typed.lambda.sandbox

import leo14.untyped.dsl2.*

fun main() {
	main_ {
		number.does { given.number.done }
		number(10).printing
	}
}

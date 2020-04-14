package leo14.untyped.typed.lambda.sandbox

import leo14.untyped.dsl2.equals_
import leo14.untyped.dsl2.main_
import leo14.untyped.dsl2.number

fun main() {
	main_ {
		number(10).equals_ { number(20) }
	}
}

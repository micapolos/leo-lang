package leo14.untyped.typed.lambda.sandbox

import leo14.untyped.dsl2.is_
import leo14.untyped.dsl2.main_
import leo14.untyped.dsl2.my
import leo14.untyped.dsl2.number

fun main() {
	main_ {
		my.number.is_ { number(123) }
		my.number
	}
}

package leo14.untyped.typed.lambda.sandbox

import leo14.untyped.dsl2.given
import leo14.untyped.dsl2.is_
import leo14.untyped.dsl2.main_
import leo14.untyped.dsl2.number

fun main() {
	main_ {
		number.is_ { given }
		number(125)
	}
}

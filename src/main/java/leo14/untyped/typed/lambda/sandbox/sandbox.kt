package leo14.untyped.typed.lambda.sandbox

import leo14.untyped.dsl2.java
import leo14.untyped.dsl2.main_
import leo14.untyped.dsl2.number
import leo14.untyped.dsl2.x

fun main() {
	main_ {
		x { number(10) }.number.java
	}
}

package leo14.untyped.typed.lambda.sandbox

import leo14.untyped.dsl2.*

fun main() =
	main_ {
		text("java.awt.Point").name.class_.java
		field { text("x").name }
	}

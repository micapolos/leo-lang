package leo14.untyped.typed.lambda.sandbox

import leo14.untyped.dsl2.*

fun main() {
	main_ {
		text("java.awt.Point").name.class_.java
		constructor {
			parameters {
				parameter { int.class_.java }
				parameter { int.class_.java }
			}
		}
		invoke {
			parameters {
				parameter { number(10).int.java }
				parameter { number(20).int.java }
			}
		}
	}
}

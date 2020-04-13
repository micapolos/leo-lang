package leo14.untyped.typed.lambda.sandbox

import leo14.untyped.dsl2.*

fun main() {
	main_ {
		text("java.lang.String").name.class_.java
		method {
			name { text("substring") }
			parameters {
				parameter { int.class_.java }
				parameter { int.class_.java }
			}
		}
		invoke {
			object_ { text("Hello, world!").java }
			parameters {
				parameter { number(7).int.java }
				parameter { number(12).int.java }
			}
		}
	}
}

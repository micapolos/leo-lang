package leo14.untyped.typed.lambda

import leo14.untyped.dsl2.*
import kotlin.test.Test

class EvalJavaTest {
	@Test
	fun javaType() {
		script_ {
			int.class_.java
		}.gives_ {
			class_ { java_(Integer.TYPE) }
		}
	}

	@Test
	fun javaClass() {
		script_ {
			text("java.lang.String").name.class_.java
		}.gives_ {
			class_ { java_(java.lang.String::class.java) }
		}
	}
}
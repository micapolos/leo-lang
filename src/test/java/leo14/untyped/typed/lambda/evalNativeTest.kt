package leo14.untyped.typed.lambda

import leo14.untyped.dsl2.*
import leo14.untyped.typed.javaName
import kotlin.test.Test

class EvalJavaTest {
	@Test
	fun javaType() {
		script_ {
			int.class_.java
		}.gives_ {
			class_ { x(Integer.TYPE.javaName) }
		}
	}
}
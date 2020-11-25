package leo21.native

import leo.base.assertNotNull
import kotlin.test.Test

class DslTest {
	@Test
	fun vector_() {
		vector(text("Hello, "), text("world!"))
			.get(number(0))
			.assertNotNull
	}

	@Test
	fun fn_() {
		fn2(v1.minus(v0))
			.assertNotNull
	}
}
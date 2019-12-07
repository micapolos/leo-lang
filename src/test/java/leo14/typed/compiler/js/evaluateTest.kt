package leo14.typed.compiler.js

import leo.base.assertEqualTo
import leo14.literal
import leo14.script
import leo14.typed.compiler.evaluate
import kotlin.test.Test

class EvaluateTest {
	@Test
	fun text() {
		emptyContext
			.evaluate(script(literal("123")))
			.assertEqualTo(null)
	}
}

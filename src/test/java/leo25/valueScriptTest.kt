package leo25

import leo.base.assertEqualTo
import leo14.lineTo
import leo14.literal
import leo14.script
import kotlin.test.Test

class ValueScriptTest {
	@Test
	fun text() {
		value("text" to value("Michał")).script.assertEqualTo(script(literal("Michał")))
	}

	@Test
	fun natives() {
		value("Michał").script.assertEqualTo(script("native"))
		value(Function(context(), body(value("foo")))).script.assertEqualTo(script("native"))
	}

	@Test
	fun struct() {
		value("point" to value("x" to value("zero" to null), "y" to value("one" to null)))
			.script
			.assertEqualTo(script("point" lineTo script("x" lineTo script("zero"), "y" lineTo script("one"))))
	}
}
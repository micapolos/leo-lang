package leo21.compiler

import leo.base.assertEqualTo
import leo14.lineTo
import leo14.literal
import leo14.script
import leo21.typed.lineTo
import leo21.typed.typed
import kotlin.test.Test

class ScriptTypedTest {
	@Test
	fun struct() {
		script(
			"point" lineTo script(
				"x" lineTo script(literal(10.0)),
				"y" lineTo script(literal(20.0))))
			.typed
			.assertEqualTo(
				typed(
					"point" lineTo typed(
						"x" lineTo typed(10.0),
						"y" lineTo typed(20.0))))
	}
}
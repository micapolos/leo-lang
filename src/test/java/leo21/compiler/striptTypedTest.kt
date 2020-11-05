package leo21.compiler

import leo.base.assertEqualTo
import leo14.lineTo
import leo14.literal
import leo14.script
import leo21.compiled.compiled
import leo21.compiled.lineTo
import kotlin.test.Test

class ScriptTypedTest {
	@Test
	fun struct() {
		script(
			"point" lineTo script(
				"x" lineTo script(literal(10.0)),
				"y" lineTo script(literal(20.0))))
			.compiled
			.assertEqualTo(
				compiled(
					"point" lineTo compiled(
						"x" lineTo compiled(10.0),
						"y" lineTo compiled(20.0))))
	}
}
package leo21.compiled

import leo.base.assertEqualTo
import kotlin.test.Test

class CompiledResolveTest {
	@Test
	fun get() {
		compiled(
			"point" lineTo compiled(
				"x" lineTo compiled(10.0),
				"y" lineTo compiled(20.0)),
			"x" lineTo compiled())
			.resolveGetOrNull!!
			.assertEqualTo(
				compiled(
					"point" lineTo compiled(
						"x" lineTo compiled(10.0),
						"y" lineTo compiled(20.0)))
					.getOrNull("x")!!)
	}
}
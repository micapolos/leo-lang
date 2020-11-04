package leo21.typed

import leo.base.assertEqualTo
import kotlin.test.Test

class TypedResolveTest {
	@Test
	fun get() {
		typed(
			"point" lineTo typed(
				"x" lineTo typed(10.0),
				"y" lineTo typed(20.0)),
			"x" lineTo typed())
			.resolveGetOrNull!!
			.assertEqualTo(
				typed(
					"point" lineTo typed(
						"x" lineTo typed(10.0),
						"y" lineTo typed(20.0)))
					.getOrNull("x")!!)
	}
}
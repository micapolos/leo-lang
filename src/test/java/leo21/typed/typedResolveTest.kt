package leo21.typed

import leo.base.assertEqualTo
import leo.base.assertNotNull
import leo21.evaluator.evaluated
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

	@Test
	fun resolvePrim() {
		typed(line(10.0), "plus" lineTo typed(20.0))
			.resolvePrimOrNull!!
			.assertNotNull // TODO()
	}
}
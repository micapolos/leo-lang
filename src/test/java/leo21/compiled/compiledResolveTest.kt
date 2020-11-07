package leo21.compiled

import leo.base.assertEqualTo
import leo21.evaluator.evaluated
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

	@Test
	fun to() {
		val compiled = compiled(
			"x" lineTo compiled(10.0),
			"point" lineTo compiled(
				"to" lineTo compiled(
					"y" lineTo compiled(20.0))))

		compiled
			.resolve
			.assertEqualTo(
				compiled.access("x")
					.plus(compiled.access("point").get("to").get("y").link.head)
					.make("point"))
	}

	@Test
	fun as_() {
		val compiled = compiled(
			"x" lineTo compiled(10.0),
			"y" lineTo compiled(20.0),
			"as" lineTo compiled(
				"point" lineTo compiled()))

		compiled
			.resolve
			.assertEqualTo(compiled.link.tail.make("point"))
	}
}
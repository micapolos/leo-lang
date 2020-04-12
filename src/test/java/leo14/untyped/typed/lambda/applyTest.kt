package leo14.untyped.typed.lambda

import leo.base.assertEqualTo
import kotlin.test.Test

class ApplyTest {
	@Test
	fun get() {
		compiled(
			"point" lineTo compiled(
				"x" lineTo 10.compiled,
				"y" lineTo 20.compiled),
			"x" lineTo emptyCompiled)
			.apply
			.eval
			.assertEqualTo(compiled("x" lineTo 10.compiled))

		compiled(
			"point" lineTo compiled(
				"x" lineTo 10.compiled,
				"y" lineTo 20.compiled),
			"y" lineTo emptyCompiled)
			.apply
			.eval
			.assertEqualTo(compiled("y" lineTo 20.compiled))
	}

	@Test
	fun make() {
		compiled(
			"point" lineTo compiled(
				"x" lineTo 10.compiled,
				"y" lineTo 20.compiled),
			"center" lineTo emptyCompiled)
			.apply
			.eval
			.assertEqualTo(
				compiled(
					"center" lineTo compiled(
						"point" lineTo compiled(
							"x" lineTo 10.compiled,
							"y" lineTo 20.compiled))))
	}
}
package leo14.untyped.typed.lambda

import leo.base.assertEqualTo
import kotlin.test.Test

class ApplyTest {
	@Test
	fun get() {
		typed(
			"point" lineTo typed(
				"x" lineTo 10.typed,
				"y" lineTo 20.typed),
			"x" lineTo emptyTyped)
			.apply
			.eval
			.assertEqualTo(typed("x" lineTo 10.typed))

		typed(
			"point" lineTo typed(
				"x" lineTo 10.typed,
				"y" lineTo 20.typed),
			"y" lineTo emptyTyped)
			.apply
			.eval
			.assertEqualTo(typed("y" lineTo 20.typed))
	}

	@Test
	fun make() {
		typed(
			"point" lineTo typed(
				"x" lineTo 10.typed,
				"y" lineTo 20.typed),
			"center" lineTo emptyTyped)
			.apply
			.eval
			.assertEqualTo(
				typed(
					"center" lineTo typed(
						"point" lineTo typed(
							"x" lineTo 10.typed,
							"y" lineTo 20.typed))))
	}
}
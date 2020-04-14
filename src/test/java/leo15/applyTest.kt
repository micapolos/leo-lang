package leo15

import leo.base.assertEqualTo
import kotlin.test.Test

class ApplyTest {
	@Test
	fun get() {
		typed(
			"x" lineTo typed(
				"point" lineTo typed(
					"x" lineTo 10.typed,
					"y" lineTo 20.typed)))
			.apply
			.eval
			.assertEqualTo(typed("x" lineTo 10.typed))

		typed(
			"y" lineTo typed(
				"point" lineTo typed(
					"x" lineTo 10.typed,
					"y" lineTo 20.typed)))
			.apply
			.eval
			.assertEqualTo(typed("y" lineTo 20.typed))
	}
}
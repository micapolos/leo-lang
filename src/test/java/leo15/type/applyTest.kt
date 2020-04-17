package leo15.type

import kotlin.test.Test

class ApplyTest {
	@Test
	fun get() {
		typed(
			"x" lineTo typed(
				"point" lineTo typed(
					"x" lineTo 10.typed,
					"y" lineTo 20.typed)))
			.asDynamic
			.applyGet!!
			.assertEvalsTo(typed("x" lineTo 10.typed))
	}
}
package leo15.type

import leo15.minusName
import leo15.plusName
import leo15.timesName
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
			.apply
			.assertEvalsTo(typed("x" lineTo 10.typed))
	}

	@Test
	fun numberOpNumber() {
		typed(2.typedLine, plusName lineTo 3.typed)
			.asDynamic
			.apply
			.assertEvalsTo(5.typed)

		typed(5.typedLine, minusName lineTo 3.typed)
			.asDynamic
			.apply
			.assertEvalsTo(2.typed)

		typed(2.typedLine, timesName lineTo 3.typed)
			.asDynamic
			.apply
			.assertEvalsTo(6.typed)
	}
}
package leo15.type

import leo.base.assertEqualTo
import leo.base.assertNull
import kotlin.test.Test

class GetTest {
	@Test
	fun get() {
		typed(
			"point" lineTo typed(
				"x" lineTo 10.typed,
				"y" lineTo 20.typed))
			.run {
				get("x")!!.assertEqualTo(typed("x" lineTo 10.typed))
				get("y")!!.assertEqualTo(typed("y" lineTo 20.typed))
				get("z").assertNull
			}

		typed(
			"point" lineTo typed(
				"x" lineTo 10.typed,
				"y" lineTo 20.typed))
			.asDynamic
			.run {
				get("x")!!.assertEvalsTo(typed("x" lineTo 10.typed))
				get("y")!!.assertEvalsTo(typed("y" lineTo 20.typed))
				get("z").assertNull
			}
	}
}
package leo15.type

import leo.base.assertNull
import kotlin.test.Test

class GetTest {
	@Test
	fun get() {
		typed(
			"point" lineTo typed(
				"x" lineTo typed("zero"),
				"y" lineTo typed("one")))
			.run {
				get("x")!!.assertEvalsTo(typed("x" lineTo typed("zero")))
				get("y")!!.assertEvalsTo(typed("y" lineTo typed("one")))
				get("z").assertNull
			}
	}
}
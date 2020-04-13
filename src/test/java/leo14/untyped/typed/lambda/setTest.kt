package leo14.untyped.typed.lambda

import leo.base.assertEqualTo
import leo.base.assertNull
import kotlin.test.Test

class SetTest {
	@Test
	fun set() {
		typed(
			"point" lineTo typed(
				"x" lineTo 10.typed,
				"y" lineTo 20.typed))
			.run {
				setOrNull("x", 30.typed)!!
					.eval
					.assertEqualTo(
						typed(
							"point" lineTo typed(
								"x" lineTo 30.typed,
								"y" lineTo 20.typed)))

				setOrNull("y", 30.typed)!!
					.eval
					.assertEqualTo(
						typed(
							"point" lineTo typed(
								"x" lineTo 10.typed,
								"y" lineTo 30.typed)))

				setOrNull("z", 30.typed).assertNull
			}
	}
}
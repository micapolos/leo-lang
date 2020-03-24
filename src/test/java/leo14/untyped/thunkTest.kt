package leo14.untyped

import leo.base.assertEqualTo
import leo14.lineTo
import leo14.literal
import leo14.script
import kotlin.test.Test

class ThunkTest {
	@Test
	fun value() {
		thunk(
			value("minus" lineTo value(literal(1))))
			.value
			.assertEqualTo(value("minus" lineTo value(literal(1))))
	}

	@Test
	fun lazyValue() {
		thunk(
			lazy(
				context(),
				script("minus" lineTo script(literal(1)))))
			.value
			.assertEqualTo(value(literal(-1)))
	}
}
package leo14.untyped

import leo.base.assertEqualTo
import leo14.lineTo
import leo14.literal
import leo14.script
import kotlin.test.Test

class LazyTest {
	@Test
	fun value() {
		lazy(context(), script("minus" lineTo script(literal(1))))
			.value
			.assertEqualTo(value(literal(-1)))
	}
}
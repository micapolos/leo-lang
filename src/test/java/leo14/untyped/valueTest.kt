package leo14.untyped

import leo.base.assertEqualTo
import leo14.literal
import org.junit.Test

class LineTest {
	@Test
	fun all() {
		value(
			line(literal(10)),
			plusName lineTo value(literal(10)))
			.assertEqualTo(
				value(
					line(literal(10)),
					plusName lineTo value(literal(10))))
	}
}
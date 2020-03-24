package leo14.untyped

import leo.base.assertEqualTo
import leo14.literal
import org.junit.Test

class LineTest {
	@Test
	fun all() {
		program(
			line(literal(10)),
			plusName lineTo program(literal(10)))
			.assertEqualTo(
				program(
					line(literal(10)),
					plusName lineTo program(literal(10))))
	}
}
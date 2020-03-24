package leo14.untyped

import leo.base.assertEqualTo
import leo14.lineTo
import leo14.literal
import leo14.script
import kotlin.test.Test

class ThunkTest {
	@Test
	fun program() {
		thunk(
			program("minus" lineTo program(literal(1))))
			.program
			.assertEqualTo(program("minus" lineTo program(literal(1))))
	}

	@Test
	fun lazyProgram() {
		thunk(
			lazy(
				context(),
				script("minus" lineTo script(literal(1)))))
			.program
			.assertEqualTo(program(literal(-1)))
	}
}
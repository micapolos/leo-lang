package leo14.untyped

import leo.base.assert
import leo13.base.negate
import leo14.literal
import kotlin.test.Test

class PatternTest {
	@Test
	fun number() {
		Pattern(program(numberName lineTo program()))
			.matches(program(line(literal(10))))
			.assert

		Pattern(program(numberName lineTo program()))
			.matches(program(line(literal("foo"))))
			.negate
			.assert
	}

	@Test
	fun text() {
		Pattern(program(textName lineTo program()))
			.matches(program(line(literal("foo"))))
			.assert

		Pattern(program(textName lineTo program()))
			.matches(program(line(literal(10))))
			.negate
			.assert
	}
}
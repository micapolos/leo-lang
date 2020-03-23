package leo14.untyped

import leo.base.assert
import leo13.base.negate
import leo14.literal
import kotlin.test.Test

class PatternTest {
	@Test
	fun number() {
		Pattern(program(numberName valueTo program()))
			.matches(program(value(literal(10))))
			.assert

		Pattern(program(numberName valueTo program()))
			.matches(program(value(literal("foo"))))
			.negate
			.assert
	}

	@Test
	fun text() {
		Pattern(program(textName valueTo program()))
			.matches(program(value(literal("foo"))))
			.assert

		Pattern(program(textName valueTo program()))
			.matches(program(value(literal(10))))
			.negate
			.assert
	}
}
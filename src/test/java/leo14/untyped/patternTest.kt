package leo14.untyped

import leo.base.assert
import leo13.base.negate
import leo14.literal
import kotlin.test.Test

class PatternTest {
	@Test
	fun number() {
		Pattern(value(numberName lineTo value()))
			.matches(value(line(literal(10))))
			.assert

		Pattern(value(numberName lineTo value()))
			.matches(value(line(literal("foo"))))
			.negate
			.assert
	}

	@Test
	fun text() {
		Pattern(value(textName lineTo value()))
			.matches(value(line(literal("foo"))))
			.assert

		Pattern(value(textName lineTo value()))
			.matches(value(line(literal(10))))
			.negate
			.assert
	}
}
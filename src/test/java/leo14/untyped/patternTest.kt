package leo14.untyped

import leo.base.assert
import leo13.base.negate
import leo14.literal
import kotlin.test.Test

class PatternTest {
	@Test
	fun number() {
		pattern(thunk(value(numberName lineTo value())))
			.matches(thunk(value(line(literal(10)))))
			.assert

		pattern(thunk(value(numberName lineTo value())))
			.matches(thunk(value(line(literal("foo")))))
			.negate
			.assert
	}

	@Test
	fun text() {
		pattern(thunk(value(textName lineTo value())))
			.matches(thunk(value(line(literal("foo")))))
			.assert

		pattern(thunk(value(textName lineTo value())))
			.matches(thunk(value(line(literal(10)))))
			.negate
			.assert
	}
}
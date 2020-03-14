package leo14.untyped

import leo.base.assert
import leo13.base.negate
import leo14.*
import kotlin.test.Test

class PatternTest {
	@Test
	fun number() {
		Pattern(link("number" lineTo script()))
			.matches(link(line(literal(10))))
			.assert

		Pattern(link("number" lineTo script()))
			.matches(link(line(literal("foo"))))
			.negate
			.assert
	}

	@Test
	fun text() {
		Pattern(link("text" lineTo script()))
			.matches(link(line(literal("foo"))))
			.assert

		Pattern(link("text" lineTo script()))
			.matches(link(line(literal(10))))
			.negate
			.assert
	}
}
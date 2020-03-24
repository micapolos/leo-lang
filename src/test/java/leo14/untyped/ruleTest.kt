package leo14.untyped

import leo.base.assertEqualTo
import leo14.lineTo
import leo14.literal
import leo14.script
import kotlin.test.Test

class RuleTest {
	@Test
	fun resolve_matchProgram() {
		Rule(
			Pattern(value("foo" lineTo value())),
			body(thunk(value("bar"))))
			.apply(context(), thunk(value("foo")))
			.assertEqualTo(thunk(value("bar")))
	}

	@Test
	fun resolve_matchFunction() {
		rule(
			pattern(value(numberName lineTo value())),
			body(script(givenName lineTo script())))
			.apply(context(), thunk(value(line(literal(10)))))
			.assertEqualTo(thunk(value(givenName lineTo value(literal(10)))))
	}

	@Test
	fun resolve_mismatch() {
		Rule(
			Pattern(value(numberName lineTo value())),
			body(thunk(value("ok"))))
			.apply(context(), thunk(value(line(literal("foo")))))
			.assertEqualTo(null)
	}
}
package leo14.untyped

import leo.base.assertEqualTo
import leo14.lineTo
import leo14.literal
import leo14.script
import kotlin.test.Test

class RuleTest {
	@Test
	fun resolve_matchProgram() {
		rule(
			pattern(thunk(value("foo" lineTo value()))),
			body(thunk(value("bar"))))
			.apply(scope(), thunk(value("foo")))
			.assertEqualTo(applied(thunk(value("bar"))))
	}

	@Test
	fun resolve_matchFunction() {
		rule(
			pattern(thunk(value(numberName lineTo value()))),
			evalBody(script(givenName lineTo script())))
			.apply(scope(), thunk(value(line(literal(10)))))
			.assertEqualTo(applied(thunk(value(givenName lineTo value(literal(10))))))
	}

	@Test
	fun resolve_mismatch() {
		rule(
			pattern(thunk(value(numberName lineTo value()))),
			body(thunk(value("ok"))))
			.apply(scope(), thunk(value(line(literal("foo")))))
			.assertEqualTo(null)
	}
}
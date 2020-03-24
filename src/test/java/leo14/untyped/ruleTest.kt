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
			Pattern(program("foo" lineTo program())),
			body(thunk(program("bar"))))
			.apply(context(), thunk(program("foo")))
			.assertEqualTo(thunk(program("bar")))
	}

	@Test
	fun resolve_matchFunction() {
		rule(
			pattern(program(numberName lineTo program())),
			body(script(givenName lineTo script())))
			.apply(context(), thunk(program(line(literal(10)))))
			.assertEqualTo(thunk(program(givenName lineTo program(literal(10)))))
	}

	@Test
	fun resolve_mismatch() {
		Rule(
			Pattern(program(numberName lineTo program())),
			body(thunk(program("ok"))))
			.apply(context(), thunk(program(line(literal("foo")))))
			.assertEqualTo(null)
	}
}
package leo14.untyped

import leo.base.assertEqualTo
import leo14.literal
import kotlin.test.Test

class RuleTest {
	@Test
	fun resolve_matchProgram() {
		Rule(
			Pattern(program("number" valueTo program())),
			body(program("ok")))
			.apply(program("number"))
			.assertEqualTo(program("ok"))
	}

	@Test
	fun resolve_matchFunction() {
		Rule(
			Pattern(program("number" valueTo program())),
			body(function(context(), program("given"))))
			.apply(program(value(literal(10))))
			.assertEqualTo(program("given" valueTo program(literal(10))))
	}

	@Test
	fun resolve_mismatch() {
		Rule(
			Pattern(program("number" valueTo program())),
			body(program("ok")))
			.apply(program(value(literal("foo"))))
			.assertEqualTo(null)
	}
}
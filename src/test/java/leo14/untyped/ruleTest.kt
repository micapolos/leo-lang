package leo14.untyped

import leo.base.assertEqualTo
import leo14.literal
import kotlin.test.Test

class RuleTest {
	@Test
	fun resolve_match_nullContext() {
		Rule(
			Pattern(program("number" valueTo program())),
			body(program("ok")))
			.apply(program(value(literal(10))))
			.assertEqualTo(program("ok"))
	}

	@Test
	fun resolve_match_context() {
		Rule(
			Pattern(program("number" valueTo program())),
			body(context().function(program("given"))))
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
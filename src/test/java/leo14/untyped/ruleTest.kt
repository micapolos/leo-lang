package leo14.untyped

import leo.base.assertEqualTo
import leo14.literal
import kotlin.test.Test

class RuleTest {
	@Test
	fun resolve_matchProgram() {
		Rule(
			Pattern(program("foo" valueTo program())),
			body(program("bar")))
			.apply(program("foo"))
			.assertEqualTo(program("bar"))
	}

	@Test
	fun resolve_matchFunction() {
		rule(
			pattern(program("number" valueTo program())),
			body(function(program("plus" valueTo program(literal(1))))))
			.apply(program(value(literal(10))))
			.assertEqualTo(program(literal(11)))
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
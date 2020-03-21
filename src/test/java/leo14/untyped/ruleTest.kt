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
			Pattern(program("foo" valueTo program())),
			body(program("bar")))
			.apply(context(), program("foo"))
			.assertEqualTo(program("bar"))
	}

	@Test
	fun resolve_matchFunction() {
		rule(
			pattern(program("number" valueTo program())),
			body(script("given" lineTo script())))
			.apply(context(), program(value(literal(10))))
			.assertEqualTo(program("given" valueTo program(literal(10))))
	}

	@Test
	fun resolve_mismatch() {
		Rule(
			Pattern(program("number" valueTo program())),
			body(program("ok")))
			.apply(context(), program(value(literal("foo"))))
			.assertEqualTo(null)
	}
}
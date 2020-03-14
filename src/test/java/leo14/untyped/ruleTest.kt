package leo14.untyped

import leo.base.assertEqualTo
import leo14.*
import kotlin.test.Test

class RuleTest {
	@Test
	fun resolve_match_nullContext() {
		Rule(
			Pattern(link("number" lineTo script())),
			body(script("ok")))
			.resolve(link(line(literal(10))))
			.assertEqualTo(script("ok"))
	}

	@Test
	fun resolve_match_context() {
		Rule(
			Pattern(link("number" lineTo script())),
			body(context().function(script("given"))))
			.resolve(link(line(literal(10))))
			.assertEqualTo(script("given" lineTo script(literal(10))))
	}

	@Test
	fun resolve_mismatch() {
		Rule(
			Pattern(link("number" lineTo script())),
			body(script("ok")))
			.resolve(link(line(literal("foo"))))
			.assertEqualTo(null)
	}
}
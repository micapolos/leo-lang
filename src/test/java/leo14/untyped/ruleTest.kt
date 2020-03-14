package leo14.untyped

import leo.base.assertEqualTo
import leo14.*
import kotlin.test.Test

class RuleTest {
	@Test
	fun resolve_match_nullContext() {
		Rule(
			Pattern(link("number" lineTo script())),
			Body(script("ok"), null))
			.resolve(link(line(literal(10))))
			.assertEqualTo(script("ok"))
	}

	@Test
	fun resolve_match_context() {
		Rule(
			Pattern(link("number" lineTo script())),
			Body(script("given"), context()))
			.resolve(link(line(literal(10))))
			.assertEqualTo(script("given" lineTo script(literal(10))))
	}

	@Test
	fun resolve_mismatch() {
		Rule(
			Pattern(link("number" lineTo script())),
			Body(script("ok"), null))
			.resolve(link(line(literal("foo"))))
			.assertEqualTo(null)
	}
}
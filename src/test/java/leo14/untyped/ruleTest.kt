package leo14.untyped

import leo.base.assertEqualTo
import leo14.*
import kotlin.test.Test

class RuleTest {
	@Test
	fun resolve_match() {
		Rule(
			Pattern(link("number" lineTo script())),
			Body(script("ok"), null))
			.resolve(link(line(literal(10))))
			.assertEqualTo(script("ok"))
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
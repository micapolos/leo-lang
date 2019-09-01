package leo13.type

import leo.base.assertEqualTo
import kotlin.test.Test

class PatternTest {
	@Test
	fun lhsOrNull() {
		pattern()
			.previousOrNull
			.assertEqualTo(null)

		pattern("one" lineTo pattern())
			.previousOrNull
			.assertEqualTo(pattern())

		pattern("one" lineTo pattern(), "two" lineTo pattern())
			.previousOrNull
			.assertEqualTo(pattern("one" lineTo pattern()))

		pattern(unsafeChoice("one" caseTo pattern(), "two" caseTo pattern()))
			.previousOrNull
			.assertEqualTo(pattern())

		pattern(arrow(type(pattern("one")), type(pattern("two"))))
			.previousOrNull
			.assertEqualTo(pattern())
	}

	@Test
	fun resolveRecursion_simple() {
		pattern("foo" lineTo rhs(recursion))
			.resolveRecursion(pattern("bar"))
			.assertEqualTo(pattern("foo" lineTo pattern("bar")))
	}

	@Test
	fun resolveRecursion_mismatch() {
		pattern("foo" lineTo pattern("bar" lineTo rhs(recursion)))
			.resolveRecursion(pattern("bar"))
			.assertEqualTo(pattern("foo" lineTo pattern("bar" lineTo rhs(recursion))))
	}

	@Test
	fun resolveRecursion_choice() {
		pattern(
			unsafeChoice(
				"node" caseTo pattern(
					"value" lineTo pattern("foo"),
					"next" lineTo rhs(recursion.recursion)),
				"empty" caseTo pattern()))
			.resolveRecursion(pattern("replaced"))
			.assertEqualTo(
				pattern(
					unsafeChoice(
						"node" caseTo pattern(
							"value" lineTo pattern("foo"),
							"next" lineTo pattern("replaced")),
						"empty" caseTo pattern())))
	}
}
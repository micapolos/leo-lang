package leo13.type.pattern

import leo.base.assertEqualTo
import kotlin.test.Test

class PatternTest {
	@Test
	fun contains() {
		pattern()
			.contains(pattern())
			.assertEqualTo(true)

		pattern(
			"zero" lineTo pattern(),
			"plus" lineTo pattern("one"))
			.contains(
				pattern(
					"zero" lineTo pattern(),
					"plus" lineTo pattern("one")))
			.assertEqualTo(true)

		pattern(
			choice(
				"zero" caseTo pattern(),
				"one" caseTo pattern()))
			.contains(
				pattern("zero" lineTo pattern()))
			.assertEqualTo(true)

		pattern(
			choice(
				"zero" caseTo pattern(),
				"one" caseTo pattern()))
			.contains(pattern(
				choice(
					"zero" caseTo pattern(),
					"one" caseTo pattern())))
			.assertEqualTo(true)

		pattern(
			choice(
				"zero" caseTo pattern(),
				"one" caseTo pattern()))
			.contains(
				pattern(
					choice(
						"one" caseTo pattern(),
						"zero" caseTo pattern())))
			.assertEqualTo(false)

		pattern(
			choice(
				"zero" caseTo pattern(),
				"one" caseTo pattern()))
			.contains(
				pattern(choice("zero" caseTo pattern())))
			.assertEqualTo(false)
	}
}
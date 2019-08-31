package leo13.type

import leo.base.assertEqualTo
import kotlin.test.Test

class ContainsTest {
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
			unsafeChoice(
				"zero" caseTo pattern(),
				"one" caseTo pattern()))
			.contains(
				pattern("zero" lineTo pattern()))
			.assertEqualTo(true)

		pattern(
			unsafeChoice(
				"zero" caseTo pattern(),
				"one" caseTo pattern()))
			.contains(
				pattern(
					unsafeChoice(
						"zero" caseTo pattern(),
						"one" caseTo pattern())))
			.assertEqualTo(true)

		pattern(
			unsafeChoice(
				"zero" caseTo pattern(),
				"one" caseTo pattern()))
			.contains(
				pattern(
					unsafeChoice(
						"one" caseTo pattern(),
						"zero" caseTo pattern())))
			.assertEqualTo(false)

		pattern(
			unsafeChoice(
				"zero" caseTo pattern(),
				"one" caseTo pattern()))
			.contains(
				pattern(
					unsafeChoice(
						"one" caseTo pattern(),
						"zero" caseTo pattern())))
			.assertEqualTo(false)
	}
}
package leo13.type

import leo.base.assertEqualTo
import kotlin.test.Test

class ContainsTest {
	@Test
	fun contains() {
		type()
			.contains(type())
			.assertEqualTo(true)

		type(
			"zero" lineTo type(),
			"plus" lineTo type("one"))
			.contains(
				type(
					"zero" lineTo type(),
					"plus" lineTo type("one")))
			.assertEqualTo(true)

		type(
			unsafeChoice(
				"zero" caseTo type(),
				"one" caseTo type()))
			.contains(
				type("zero" lineTo type()))
			.assertEqualTo(true)

		type(
			unsafeChoice(
				"zero" caseTo type(),
				"one" caseTo type()))
			.contains(
				type(
					unsafeChoice(
						"zero" caseTo type(),
						"one" caseTo type())))
			.assertEqualTo(true)

		type(
			unsafeChoice(
				"zero" caseTo type(),
				"one" caseTo type()))
			.contains(
				type(
					unsafeChoice(
						"one" caseTo type(),
						"zero" caseTo type())))
			.assertEqualTo(false)

		type(
			unsafeChoice(
				"zero" caseTo type(),
				"one" caseTo type()))
			.contains(
				type(
					unsafeChoice(
						"one" caseTo type(),
						"zero" caseTo type())))
			.assertEqualTo(false)
	}
}
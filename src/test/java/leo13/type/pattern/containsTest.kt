package leo13.type.pattern

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
			choice(
				"zero" caseTo type(),
				"one" caseTo type()))
			.contains(
				type("zero" lineTo type()))
			.assertEqualTo(true)

		type(
			choice(
				"zero" caseTo type(),
				"one" caseTo type()))
			.contains(
				type(
					choice(
						"zero" caseTo type(),
						"one" caseTo type())))
			.assertEqualTo(true)

		type(
			choice(
				"zero" caseTo type(),
				"one" caseTo type()))
			.contains(
				type(
					choice(
						"one" caseTo type(),
						"zero" caseTo type())))
			.assertEqualTo(false)

		type(
			choice(
				"zero" caseTo type(),
				"one" caseTo type()))
			.contains(
				type(
					choice(
						"one" caseTo type(),
						"zero" caseTo type())))
			.assertEqualTo(false)
	}
}
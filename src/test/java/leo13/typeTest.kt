package leo13

import leo.base.assertEqualTo
import kotlin.test.Test

class TypeTest {
	@Test
	fun string() {
		type().toString().assertEqualTo("")

		type("one" lineTo type())
			.toString()
			.assertEqualTo("one()")

		type("one" lineTo type(), "two" lineTo type())
			.toString()
			.assertEqualTo("one()two()")

		type("one" lineTo type("two" lineTo type()))
			.toString()
			.assertEqualTo("one(two())")

		type("or" lineTo type())
			.toString()
			.assertEqualTo("or()")

		type("or" lineTo type("one" lineTo type()))
			.toString()
			.assertEqualTo("meta(or(one()))")

		type("or" lineTo type("one" lineTo type(), "two" lineTo type()))
			.toString()
			.assertEqualTo("or(one()two())")

		type(choice("one" caseTo type(), "two" caseTo type()))
			.toString()
			.assertEqualTo("one()or(two())")

		type(choice("one" caseTo type(), "two" caseTo type()), "three" lineTo type())
			.toString()
			.assertEqualTo("one()or(two())three()")

		type(choice("one" caseTo type(), "two" caseTo type()), "or" lineTo type("three" lineTo type()))
			.toString()
			.assertEqualTo("one()or(two())meta(or(three()))")
	}

	@Test
	fun parse() {
		script()
			.type
			.assertEqualTo(type())

		script("one" lineTo script())
			.type
			.assertEqualTo(type("one" lineTo type()))

		script("one" lineTo script("two" lineTo script()))
			.type
			.assertEqualTo(type("one" lineTo type("two" lineTo type())))

		script("one" lineTo script(), "two" lineTo script())
			.type
			.assertEqualTo(type("one" lineTo type(), "two" lineTo type()))

		script(
			"one" lineTo script(),
			"or" lineTo script("two" lineTo script()))
			.type
			.assertEqualTo(type(choice("one" caseTo type(), "two" caseTo type())))

		script(
			"one" lineTo script(),
			"or" lineTo script("two" lineTo script()),
			"or" lineTo script("three" lineTo script()))
			.type
			.assertEqualTo(type(choice("one" caseTo type(), "two" caseTo type(), "three" caseTo type())))

		script(
			"one" lineTo script(),
			"or" lineTo script("two" lineTo script()),
			"three" lineTo script())
			.type
			.assertEqualTo(type(choice("one" caseTo type(), "two" caseTo type()), "three" lineTo type()))
	}

	@Test
	fun matches() {
		type()
			.apply {
				matches(script()).assertEqualTo(true)
				matches(script("one" lineTo script())).assertEqualTo(false)
			}

		type("one" lineTo type())
			.apply {
				matches(script()).assertEqualTo(false)
				matches(script("one" lineTo script())).assertEqualTo(true)
				matches(script("two" lineTo script())).assertEqualTo(false)
			}

		type("one" lineTo type("two" lineTo type()))
			.apply {
				matches(script("one" lineTo script("two" lineTo script()))).assertEqualTo(true)
			}

		type("one" lineTo type(), "two" lineTo type())
			.apply {
				matches(script("one" lineTo script(), "two" lineTo script())).assertEqualTo(true)
			}

		type(choice("one" caseTo type(), "two" caseTo type()))
			.apply {
				matches(script("one" lineTo script())).assertEqualTo(true)
				matches(script("two" lineTo script())).assertEqualTo(true)
				matches(script("three" lineTo script())).assertEqualTo(false)
			}
	}

	@Test
	fun contains() {
		type()
			.apply {
				contains(type()).assertEqualTo(true)
				contains(type("one" lineTo type())).assertEqualTo(false)
			}

		type("one" lineTo type())
			.apply {
				contains(type()).assertEqualTo(false)
				contains(type("one" lineTo type())).assertEqualTo(true)
				contains(type("two" lineTo type())).assertEqualTo(false)
			}

		type("one" lineTo type(), "two" lineTo type())
			.apply {
				contains(type("one" lineTo type(), "two" lineTo type())).assertEqualTo(true)
				contains(type("one" lineTo type())).assertEqualTo(false)
				contains(type("two" lineTo type(), "one" lineTo type())).assertEqualTo(false)
				contains(type("one" lineTo type(), "two" lineTo type(), "three" lineTo type())).assertEqualTo(false)
			}

		type(choice("one" caseTo type(), "two" caseTo type()))
			.apply {
				contains(type("one" lineTo type())).assertEqualTo(true)
				contains(type("two" lineTo type())).assertEqualTo(true)
				contains(type(choice("one" caseTo type(), "two" caseTo type()))).assertEqualTo(true)
				contains(type(choice("one" caseTo type(), "three" caseTo type()))).assertEqualTo(false)
				contains(type(choice("one" caseTo type(), "two" caseTo type(), "three" caseTo type()))).assertEqualTo(false)
				contains(type("three" lineTo type())).assertEqualTo(false)
			}

		type("bit" lineTo type(choice("zero" caseTo type(), "one" caseTo type())))
			.apply {
				contains(type("bit" lineTo type("zero" lineTo type()))).assertEqualTo(true)
				contains(type("bit" lineTo type("one" lineTo type()))).assertEqualTo(true)
				contains(type("bit" lineTo type("two" lineTo type()))).assertEqualTo(false)
			}
	}
}
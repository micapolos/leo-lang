package leo13.type

import leo.base.assertEqualTo
import leo13.assertAsScriptLineWorks
import leo13.assertEqualsToScriptLine
import leo13.lineTo
import leo13.script
import kotlin.test.Test

class TypeTest {
	@Test
	fun asScriptLine() {
		type()
			.assertEqualsToScriptLine("type()")

		type(unsafeChoice())
			.assertEqualsToScriptLine("type(choice(null()))")

		type("zero" lineTo type(), "one" lineTo type())
			.assertEqualsToScriptLine("type(zero()one())")

		type(unsafeChoice(), "zero" lineTo type(), "one" lineTo type())
			.assertEqualsToScriptLine("type(choice(null())zero()one())")
	}

	@Test
	fun typeOrNull() {
		type()
			.assertAsScriptLineWorks { typeOrNull }

		type(unsafeChoice())
			.assertAsScriptLineWorks { typeOrNull }

		type("zero" lineTo type(), "one" lineTo type())
			.assertAsScriptLineWorks { typeOrNull }

		type(unsafeChoice(), "zero" lineTo type(), "one" lineTo type())
			.assertAsScriptLineWorks { typeOrNull }
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

		type(unsafeChoice("one" eitherTo type(), "two" eitherTo type()))
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

		type(unsafeChoice("one" eitherTo type(), "two" eitherTo type()))
			.apply {
				contains(type("one" lineTo type())).assertEqualTo(true)
				contains(type("two" lineTo type())).assertEqualTo(true)
				contains(type(unsafeChoice("one" eitherTo type(), "two" eitherTo type()))).assertEqualTo(true)
				contains(type(unsafeChoice("one" eitherTo type(), "three" eitherTo type()))).assertEqualTo(false)
				contains(type(unsafeChoice("one" eitherTo type(), "two" eitherTo type(), "three" eitherTo type()))).assertEqualTo(false)
				contains(type("three" lineTo type())).assertEqualTo(false)
			}

		type("bit" lineTo type(unsafeChoice("zero" eitherTo type(), "one" eitherTo type())))
			.apply {
				contains(type("bit" lineTo type("zero" lineTo type()))).assertEqualTo(true)
				contains(type("bit" lineTo type("one" lineTo type()))).assertEqualTo(true)
				contains(type("bit" lineTo type("two" lineTo type()))).assertEqualTo(false)
			}
	}
}
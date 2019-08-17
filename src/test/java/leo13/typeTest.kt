package leo13

import leo.base.assertEqualTo
import leo.base.indexed
import kotlin.test.Test
import kotlin.test.assertFails

class TypeTest {
	@Test
	fun parse() {
		script()
			.type
			.assertEqualTo(type())

		script("one" lineTo script())
			.type
			.assertEqualTo(type(choice("one" lineTo type())))

		script("one" lineTo script("two" lineTo script()))
			.type
			.assertEqualTo(type(choice("one" lineTo type(choice("two" lineTo type())))))

		script("one" lineTo script(), "two" lineTo script())
			.type
			.assertEqualTo(type(choice("one" lineTo type()), choice("two" lineTo type())))

		script("either" lineTo script())
			.type
			.assertEqualTo(type(choice("either" lineTo type())))

		script("either" lineTo script("one" lineTo script()))
			.type
			.assertEqualTo(type(choice("one" lineTo type())))

		script(
			"either" lineTo script("one" lineTo script()),
			"either" lineTo script("two" lineTo script()))
			.type
			.assertEqualTo(type(choice("one" lineTo type(), "two" lineTo type())))

		script(
			"either" lineTo script("one" lineTo script()),
			"either" lineTo script("two" lineTo script()),
			"negate" lineTo script())
			.type
			.assertEqualTo(
				type(
					choice("either" lineTo type(choice("one" lineTo type()))),
					choice("either" lineTo type(choice("two" lineTo type()))),
					choice("negate" lineTo type())))

		script("either" lineTo script("one" lineTo script(), "two" lineTo script()))
			.type
			.assertEqualTo(type(choice(
				"either" lineTo type(
					choice("one" lineTo type()),
					choice("two" lineTo type())))))

		script(
			"one" lineTo script(),
			"either" lineTo script(
				"two" lineTo script()))
			.type
			.assertEqualTo(
				type(
					choice("one" lineTo type()),
					choice(
						"either" lineTo type(
							choice(
								"two" lineTo type())))))
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

		type(choice("one" lineTo type(), "two" lineTo type()))
			.apply {
				matches(script("one" lineTo script())).assertEqualTo(true)
				matches(script("two" lineTo script())).assertEqualTo(true)
				matches(script("three" lineTo script())).assertEqualTo(false)
			}
	}

	@Test
	fun scriptToValue() {
		type()
			.apply {
				value(script()).assertEqualTo(value())
				assertFails { value(script("one" lineTo script())) }
			}

		type(choice("one" lineTo type()))
			.apply {
				value(script("one" lineTo script())).assertEqualTo(value(0 lineTo value()))
				assertFails { value(script("two" lineTo script())) }
				assertFails { value(script()) }
			}

		type(choice("one" lineTo type(), "two" lineTo type()))
			.apply {
				value(script("one" lineTo script())).assertEqualTo(value(1 lineTo value()))
				value(script("two" lineTo script())).assertEqualTo(value(0 lineTo value()))
				assertFails { value(script("three" lineTo script())) }
			}

		type(
			choice("one" lineTo type()),
			choice("two" lineTo type()))
			.apply {
				value(script("one" lineTo script(), "two" lineTo script()))
					.assertEqualTo(value(0 lineTo value(), 0 lineTo value()))
				assertFails { value(script("one" lineTo script())) }
				assertFails { value(script("two" lineTo script())) }
				assertFails { value(script("one" lineTo script(), "three" lineTo script())) }
				assertFails { value(script("one" lineTo script(), "two" lineTo script(), "three" lineTo script())) }
			}
		type(
			choice(
				"one" lineTo type(),
				"two" lineTo type()),
			choice(
				"one" lineTo type(),
				"two" lineTo type()))
			.apply {
				value(script("one" lineTo script(), "one" lineTo script()))
					.assertEqualTo(value(1 lineTo value(), 1 lineTo value()))
				value(script("one" lineTo script(), "two" lineTo script()))
					.assertEqualTo(value(1 lineTo value(), 0 lineTo value()))
				value(script("two" lineTo script(), "one" lineTo script()))
					.assertEqualTo(value(0 lineTo value(), 1 lineTo value()))
				value(script("two" lineTo script(), "two" lineTo script()))
					.assertEqualTo(value(0 lineTo value(), 0 lineTo value()))
				assertFails { value(script("one" lineTo script())) }
				assertFails { value(script("one" lineTo script(), "one" lineTo script(), "one" lineTo script())) }
				assertFails { value(script("one" lineTo script(), "three" lineTo script())) }
				assertFails { value(script("three" lineTo script(), "one" lineTo script())) }
			}
	}

	@Test
	fun valueToScript() {
		type()
			.apply {
				script(value()).assertEqualTo(script())
				assertFails { script(value(0 lineTo value())) }
			}

		type(choice("one" lineTo type()))
			.apply {
				script(value(0 lineTo value())).assertEqualTo(script("one" lineTo script()))
				assertFails { script(value()) }
				assertFails { script(value(1 lineTo value())) }
				assertFails { script(value(0 lineTo value(), 1 lineTo value())) }
			}

		type(choice("one" lineTo type(), "two" lineTo type()))
			.apply {
				script(value(0 lineTo value())).assertEqualTo(script("two" lineTo script()))
				script(value(1 lineTo value())).assertEqualTo(script("one" lineTo script()))
				assertFails { script(value()) }
				assertFails { script(value(2 lineTo value())) }
				assertFails { script(value(0 lineTo value(), 0 lineTo value())) }
			}

		type(choice("one" lineTo type()), choice("two" lineTo type()))
			.apply {
				script(value(0 lineTo value(), 0 lineTo value()))
					.assertEqualTo(script("one" lineTo script(), "two" lineTo script()))
				assertFails { script(value()) }
				assertFails { script(value(0 lineTo value())) }
				assertFails { script(value(0 lineTo value(), 0 lineTo value(), 0 lineTo value())) }
				assertFails { script(value(0 lineTo value(), 1 lineTo value())) }
			}
	}

	@Test
	fun access() {
		type(choice(
			"vec" lineTo type(
				choice("x" lineTo type(choice("one" lineTo type()))),
				choice("y" lineTo type(choice("two" lineTo type()))))))
			.apply {
				accessOrNull("x").assertEqualTo(access(1, type(choice("x" lineTo type(choice("one" lineTo type()))))))
				accessOrNull("y").assertEqualTo(access(0, type(choice("y" lineTo type(choice("two" lineTo type()))))))
				accessOrNull("z").assertEqualTo(null)
			}
	}

	@Test
	fun indexedRhsOrNull() {
		type(
			choice("x" lineTo type(choice("one" lineTo type()))),
			choice("y" lineTo type(choice("two" lineTo type()))))
			.apply {
				indexedRhsOrNull("x").assertEqualTo(1 indexed type(choice("one" lineTo type())))
				indexedRhsOrNull("y").assertEqualTo(0 indexed type(choice("two" lineTo type())))
				indexedRhsOrNull("z").assertEqualTo(null)
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

		type(choice("one" lineTo type(), "two" lineTo type()))
			.apply {
				contains(type("one" lineTo type())).assertEqualTo(true)
				contains(type("two" lineTo type())).assertEqualTo(true)
				contains(type(choice("one" lineTo type(), "two" lineTo type()))).assertEqualTo(true)
				contains(type(choice("one" lineTo type(), "three" lineTo type()))).assertEqualTo(false)
				contains(type(choice("one" lineTo type(), "two" lineTo type(), "three" lineTo type()))).assertEqualTo(false)
				contains(type("three" lineTo type())).assertEqualTo(false)
			}

		type("bit" lineTo type(choice("zero" lineTo type(), "one" lineTo type())))
			.apply {
				contains(type("bit" lineTo type("zero" lineTo type()))).assertEqualTo(true)
				contains(type("bit" lineTo type("one" lineTo type()))).assertEqualTo(true)
				contains(type("bit" lineTo type("two" lineTo type()))).assertEqualTo(false)
			}
	}
}
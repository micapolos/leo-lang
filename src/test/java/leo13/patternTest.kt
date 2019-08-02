package leo13

import leo.base.assertEqualTo
import kotlin.test.Test
import kotlin.test.assertFails

class PatternTest {
	@Test
	fun parse() {
		script()
			.pattern
			.assertEqualTo(pattern())

		script("one" lineTo script())
			.pattern
			.assertEqualTo(pattern(choice("one" caseTo pattern())))

		script("one" lineTo script("two" lineTo script()))
			.pattern
			.assertEqualTo(pattern(choice("one" caseTo pattern(choice("two" caseTo pattern())))))

		script("one" lineTo script(), "two" lineTo script())
			.pattern
			.assertEqualTo(pattern(choice("one" caseTo pattern()), choice("two" caseTo pattern())))

		script("either" lineTo script())
			.pattern
			.assertEqualTo(pattern(choice("either" caseTo pattern())))

		script("either" lineTo script("one" lineTo script()))
			.pattern
			.assertEqualTo(pattern(choice("one" caseTo pattern())))

		script(
			"either" lineTo script("one" lineTo script()),
			"either" lineTo script("two" lineTo script()))
			.pattern
			.assertEqualTo(pattern(choice("one" caseTo pattern(), "two" caseTo pattern())))

		script(
			"either" lineTo script("one" lineTo script()),
			"either" lineTo script("two" lineTo script()),
			"negate" lineTo script())
			.pattern
			.assertEqualTo(
				pattern(
					choice("one" caseTo pattern(), "two" caseTo pattern()),
					choice("negate" caseTo pattern())))

		script("either" lineTo script("one" lineTo script(), "two" lineTo script()))
			.pattern
			.assertEqualTo(pattern(choice(
				"either" caseTo pattern(
					choice("one" caseTo pattern()),
					choice("two" caseTo pattern())))))

		script(
			"one" lineTo script(),
			"either" lineTo script(
				"two" lineTo script()))
			.pattern
			.assertEqualTo(
				pattern(
					choice("one" caseTo pattern()),
					choice(
						"either" caseTo pattern(
							choice(
								"two" caseTo pattern())))))
	}

	@Test
	fun isConstant() {
		pattern()
			.isConstant
			.assertEqualTo(true)

		pattern(choice("one" caseTo pattern()))
			.isConstant
			.assertEqualTo(true)

		pattern(choice("one" caseTo pattern()), choice("two" caseTo pattern()))
			.isConstant
			.assertEqualTo(true)

		pattern(choice("one" caseTo pattern(), "two" caseTo pattern()))
			.isConstant
			.assertEqualTo(false)
	}

	@Test
	fun scriptToValue() {
		pattern()
			.apply {
				value(script()).assertEqualTo(value())
				assertFails { value(script("one" lineTo script())) }
			}

		pattern(choice("one" caseTo pattern()))
			.apply {
				value(script("one" lineTo script())).assertEqualTo(value(0 lineTo value()))
				assertFails { value(script("two" lineTo script())) }
				assertFails { value(script()) }
			}

		pattern(choice("one" caseTo pattern(), "two" caseTo pattern()))
			.apply {
				value(script("one" lineTo script())).assertEqualTo(value(1 lineTo value()))
				value(script("two" lineTo script())).assertEqualTo(value(0 lineTo value()))
				assertFails { value(script("three" lineTo script())) }
			}

		pattern(
			choice("one" caseTo pattern()),
			choice("two" caseTo pattern()))
			.apply {
				value(script("one" lineTo script(), "two" lineTo script()))
					.assertEqualTo(value(0 lineTo value(), 0 lineTo value()))
				assertFails { value(script("one" lineTo script())) }
				assertFails { value(script("two" lineTo script())) }
				assertFails { value(script("one" lineTo script(), "three" lineTo script())) }
				assertFails { value(script("one" lineTo script(), "two" lineTo script(), "three" lineTo script())) }
			}
		pattern(
			choice(
				"one" caseTo pattern(),
				"two" caseTo pattern()),
			choice(
				"one" caseTo pattern(),
				"two" caseTo pattern()))
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
		pattern()
			.apply {
				script(value()).assertEqualTo(script())
				assertFails { script(value(0 lineTo value())) }
			}

		pattern(choice("one" caseTo pattern()))
			.apply {
				script(value(0 lineTo value())).assertEqualTo(script("one" lineTo script()))
				assertFails { script(value()) }
				assertFails { script(value(1 lineTo value())) }
				assertFails { script(value(0 lineTo value(), 1 lineTo value())) }
			}

		pattern(choice("one" caseTo pattern(), "two" caseTo pattern()))
			.apply {
				script(value(0 lineTo value())).assertEqualTo(script("two" lineTo script()))
				script(value(1 lineTo value())).assertEqualTo(script("one" lineTo script()))
				assertFails { script(value()) }
				assertFails { script(value(2 lineTo value())) }
				assertFails { script(value(0 lineTo value(), 0 lineTo value())) }
			}

		pattern(choice("one" caseTo pattern()), choice("two" caseTo pattern()))
			.apply {
				script(value(0 lineTo value(), 0 lineTo value()))
					.assertEqualTo(script("one" lineTo script(), "two" lineTo script()))
				assertFails { script(value()) }
				assertFails { script(value(0 lineTo value())) }
				assertFails { script(value(0 lineTo value(), 0 lineTo value(), 0 lineTo value())) }
				assertFails { script(value(0 lineTo value(), 1 lineTo value())) }
			}
	}
}
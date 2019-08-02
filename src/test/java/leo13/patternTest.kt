package leo13

import leo.base.assertEqualTo
import kotlin.test.Test

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
}
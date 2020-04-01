package leo14.untyped

import leo.base.assertEqualTo
import leo14.lineTo
import leo14.script
import kotlin.test.Test

class MatchesTest {
	@Test
	fun strict_match() {
		thunk(value("foo"))
			.matches(thunk(value("foo")))
			.assertEqualTo(true)
	}

	@Test
	fun strict_mismatch() {
		thunk(value("foo"))
			.matches(thunk(value("bar")))
			.assertEqualTo(false)
	}

	@Test
	fun either_match1() {
		thunk(value("either" lineTo value(
			"true" lineTo value(),
			"false" lineTo value())))
			.matches(thunk(value("true")))
			.assertEqualTo(true)
	}

	@Test
	fun either_match2() {
		thunk(value("either" lineTo value(
			"true" lineTo value(),
			"false" lineTo value())))
			.matches(thunk(value("false")))
			.assertEqualTo(true)
	}

	@Test
	fun either_mismatch() {
		thunk(
			value(
				"either" lineTo value(
					"true" lineTo value(),
					"false" lineTo value())))
			.matches(thunk(value("zoo")))
			.assertEqualTo(false)
	}

	@Test
	fun lazy_value() {
		thunk(value("foo" lineTo thunk(value())))
			.matches(thunk(value("foo" lineTo thunk(lazy(scope(), script("foo"))))))
			.assertEqualTo(false)
	}

	@Test
	fun lazy_pattern() {
		thunk(lazy(scope(), script("foo")))
			.matches(thunk(value("foo")))
			.assertEqualTo(true)
	}

	@Test
	fun lazy_recursePattern() {
		thunk(
			lazy(
				scope(),
				script(
					"either" lineTo script(
						"stop" lineTo script(),
						"foo" lineTo script("recurse")))))
			.matches(thunk(value("stop")))
			.assertEqualTo(true)
	}
}
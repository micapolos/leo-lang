package leo14.untyped

import leo.base.assertEqualTo
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
	fun lazy() {
		thunk(value("foo" lineTo thunk(lazy(scope(), script("foo")))))
			.matches(thunk(value("foo" lineTo thunk(lazy(scope(), script("foo"))))))
			.assertEqualTo(false)
	}
}
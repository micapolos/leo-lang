package leo14.untyped

import leo.base.assertEqualTo
import kotlin.test.Test

class BodyTest {
	@Test
	fun apply_nullContext() {
		body(program("foo"))
			.apply(program("arg" valueTo program()))
			.assertEqualTo(program("foo"))
	}

	@Test
	fun apply_nonNullContext() {
		body(context().function(program("given")))
			.apply(program("foo" valueTo program()))
			.assertEqualTo(program("given" valueTo program("foo")))
	}
}
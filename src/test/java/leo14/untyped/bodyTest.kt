package leo14.untyped

import leo.base.assertEqualTo
import leo14.lineTo
import leo14.link
import leo14.script
import kotlin.test.Test

class BodyTest {
	@Test
	fun apply_nullContext() {
		body(script("foo"))
			.apply(link("arg" lineTo script()))
			.assertEqualTo(script("foo"))
	}

	@Test
	fun apply_nonNullContext() {
		body(context().function(script("given")))
			.apply(link("foo" lineTo script()))
			.assertEqualTo(script("given" lineTo script("foo")))
	}
}
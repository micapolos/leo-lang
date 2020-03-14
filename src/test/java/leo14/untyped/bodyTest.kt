package leo14.untyped

import leo.base.assertEqualTo
import leo14.lineTo
import leo14.link
import leo14.script
import kotlin.test.Test

class BodyTest {
	@Test
	fun apply_nullContext() {
		Body(script("foo"), null)
			.apply(link("arg" lineTo script()))
			.assertEqualTo(script("foo"))
	}

	@Test
	fun apply_nonNullContext() {
		Body(script("foo"), context())
			.apply(link("given" lineTo script()))
			.assertEqualTo(script("given" lineTo script("foo")))
	}
}
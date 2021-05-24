package leo25

import leo.base.assertEqualTo
import leo14.script
import kotlin.test.Test

class FunctionTest {
	@Test
	fun apply() {
		resolver().function(body(script("name")))
			.applyLeo(value("name" fieldTo value("foo"))).get
			.assertEqualTo(value("name" fieldTo value("foo")))
	}
}
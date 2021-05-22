package leo25

import leo.base.assertEqualTo
import leo14.lineTo
import leo14.script
import kotlin.test.Test

class FunctionTest {
	@Test
	fun apply() {
		dictionary().function(body(script(getName lineTo script("name"))))
			.apply(value("name" fieldTo value("foo")))
			.assertEqualTo(value("name" fieldTo value("foo")))
	}
}
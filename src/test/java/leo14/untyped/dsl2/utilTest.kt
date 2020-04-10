package leo14.untyped.dsl2

import leo.base.assertEqualTo
import leo14.invoke
import leo14.leo
import kotlin.test.Test

class UtilTest {
	@Test
	fun utilScript_() {
		script_ {
			text("Hello, ")
			plus { text("world!") }
		}.assertEqualTo(leo("Hello, ", "plus"("world!")))
	}
}
package leo32

import leo.base.assertEqualTo
import kotlin.test.Test

class ScriptTest {
	@Test
	fun script() {
		script {
			this
				.writeField("one")
				.writeField("plus", "one")
				.writeField("times", "two")
		}.assertEqualTo("one  plus one   times two   ".script)
	}
}
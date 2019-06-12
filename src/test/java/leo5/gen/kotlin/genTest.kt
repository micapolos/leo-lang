package leo5.gen.kotlin

import leo.base.appendableString
import leo.base.assertEqualTo
import leo5.script.lineTo
import leo5.script.script
import kotlin.test.Test

class GenTest {
	@Test
	fun yooo() {
		appendableString {
			it.appendFields(script(
				"radius" lineTo script("float" lineTo script()),
				"float" lineTo script()))
		}.assertEqualTo("")
	}
}

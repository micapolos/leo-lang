package leo13.script

import leo.base.assertEqualTo
import leo13.lhs
import leo13.lineTo
import leo13.script
import kotlin.test.Test

class OpTest {
	@Test
	fun asScriptLine() {
		op(lhs).asScriptLine.assertEqualTo("op" lineTo script("lhs" lineTo script()))
	}
}
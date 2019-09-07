package leo13.base

import leo.base.assertEqualTo
import leo13.script.lineTo
import leo13.script.script
import leo13.scripter.scriptLine
import kotlin.test.Test

class BitTest {
	@Test
	fun type() {
		bitScripter
			.scriptLine(zeroBit)
			.assertEqualTo("bit" lineTo script("zero"))
	}
}
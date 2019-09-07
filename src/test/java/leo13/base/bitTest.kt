package leo13.base

import leo.base.assertEqualTo
import leo13.base.type.scriptLine
import leo13.script.lineTo
import leo13.script.script
import kotlin.test.Test

class BitTest {
	@Test
	fun type() {
		bitType
			.scriptLine(zeroBit)
			.assertEqualTo("bit" lineTo script("zero"))
	}
}
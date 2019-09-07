package leo13.base

import leo.base.assertEqualTo
import leo13.script.lineTo
import leo13.script.script
import leo13.scripter.script
import leo13.scripter.scriptLine
import kotlin.test.Test

class ByteTest {
	@Test
	fun leo() {
		byteScripter
			.scriptLine(leo.base.byte(9))
			.assertEqualTo(
				"byte" lineTo script(
					"first" lineTo bitScripter.script(zeroBit),
					"second" lineTo bitScripter.script(zeroBit),
					"third" lineTo bitScripter.script(zeroBit),
					"fourth" lineTo bitScripter.script(zeroBit),
					"fifth" lineTo bitScripter.script(oneBit),
					"sixth" lineTo bitScripter.script(zeroBit),
					"seventh" lineTo bitScripter.script(zeroBit),
					"eight" lineTo bitScripter.script(oneBit)))
	}
}
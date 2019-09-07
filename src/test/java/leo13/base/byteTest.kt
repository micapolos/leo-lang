package leo13.base

import leo.base.assertEqualTo
import leo13.script.lineTo
import leo13.script.script
import leo13.script.scriptLine
import kotlin.test.Test

class ByteTest {
	@Test
	fun leo() {
		byteWriter
			.scriptLine(leo.base.byte(9))
			.assertEqualTo(
				"byte" lineTo script(
					"first" lineTo bitWriter.script(zeroBit),
					"second" lineTo bitWriter.script(zeroBit),
					"third" lineTo bitWriter.script(zeroBit),
					"fourth" lineTo bitWriter.script(zeroBit),
					"fifth" lineTo bitWriter.script(oneBit),
					"sixth" lineTo bitWriter.script(zeroBit),
					"seventh" lineTo bitWriter.script(zeroBit),
					"eight" lineTo bitWriter.script(oneBit)))
	}
}
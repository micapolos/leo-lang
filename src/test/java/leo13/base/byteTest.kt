package leo13.base

import leo.base.assertEqualTo
import leo13.base.type.script
import leo13.base.type.scriptLine
import leo13.script.lineTo
import leo13.script.script
import kotlin.test.Test

class ByteTest {
	@Test
	fun leo() {
		byteType
			.scriptLine(leo.base.byte(9))
			.assertEqualTo(
				"byte" lineTo script(
					"first" lineTo bitType.script(zeroBit),
					"second" lineTo bitType.script(zeroBit),
					"third" lineTo bitType.script(zeroBit),
					"fourth" lineTo bitType.script(zeroBit),
					"fifth" lineTo bitType.script(oneBit),
					"sixth" lineTo bitType.script(zeroBit),
					"seventh" lineTo bitType.script(zeroBit),
					"eight" lineTo bitType.script(oneBit)))
	}
}
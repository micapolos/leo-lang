package leo13.base

import leo.base.assertEqualTo
import leo13.base.type.scriptLine
import leo13.base.type.unsafeValue
import leo13.script.lineTo
import leo13.script.script
import leo9.stack
import kotlin.test.Test

class StackTest {
	@Test
	fun scriptLine() {
		stackType(bitType)
			.scriptLine(stack(zeroBit, oneBit, oneBit))
			.assertEqualTo(
				bitType.name lineTo script(
					"list" lineTo script(
						bitType.scriptLine(zeroBit),
						bitType.scriptLine(oneBit),
						bitType.scriptLine(oneBit))))
	}

	@Test
	fun unsafeValue() {
		stackType(bitType)
			.unsafeValue(
				bitType.name lineTo script(
					"list" lineTo script(
						bitType.scriptLine(zeroBit),
						bitType.scriptLine(oneBit),
						bitType.scriptLine(oneBit))))
			.assertEqualTo(stack(zeroBit, oneBit, oneBit))
	}
}
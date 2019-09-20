package leo13.base

import leo.base.assertEqualTo
import leo13.script.lineTo
import leo13.script.script
import leo13.script.scriptLine
import leo13.script.unsafeValue
import leo13.stack
import kotlin.test.Test

class StackTest {
	@Test
	fun scriptLine() {
		stackWriter(bitWriter)
			.scriptLine(stack(zeroBit, oneBit, oneBit))
			.assertEqualTo(
				bitWriter.name lineTo script(
					"list" lineTo script(
						bitWriter.scriptLine(zeroBit),
						bitWriter.scriptLine(oneBit),
						bitWriter.scriptLine(oneBit))))
	}

	@Test
	fun unsafeValue() {
		stackReader(bitReader)
			.unsafeValue(
				bitWriter.name lineTo script(
					"list" lineTo script(
						bitWriter.scriptLine(zeroBit),
						bitWriter.scriptLine(oneBit),
						bitWriter.scriptLine(oneBit))))
			.assertEqualTo(stack(zeroBit, oneBit, oneBit))
	}
}
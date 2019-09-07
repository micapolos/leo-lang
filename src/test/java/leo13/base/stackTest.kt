package leo13.base

import leo.base.assertEqualTo
import leo13.script.lineTo
import leo13.script.script
import leo13.scripter.scriptLine
import leo13.scripter.unsafeValue
import leo9.stack
import kotlin.test.Test

class StackTest {
	@Test
	fun scriptLine() {
		stackType(bitScripter)
			.scriptLine(stack(zeroBit, oneBit, oneBit))
			.assertEqualTo(
				bitScripter.name lineTo script(
					"list" lineTo script(
						bitScripter.scriptLine(zeroBit),
						bitScripter.scriptLine(oneBit),
						bitScripter.scriptLine(oneBit))))
	}

	@Test
	fun unsafeValue() {
		stackType(bitScripter)
			.unsafeValue(
				bitScripter.name lineTo script(
					"list" lineTo script(
						bitScripter.scriptLine(zeroBit),
						bitScripter.scriptLine(oneBit),
						bitScripter.scriptLine(oneBit))))
			.assertEqualTo(stack(zeroBit, oneBit, oneBit))
	}
}
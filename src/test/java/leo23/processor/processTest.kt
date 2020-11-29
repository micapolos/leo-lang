package leo23.processor

import leo.base.assertEqualTo
import leo13.stack
import leo14.line
import leo14.literal
import leo14.script
import leo23.type.numberType
import leo23.type.textType
import kotlin.test.Test

class ProcessTest {
	@Test
	fun empty() {
		script()
			.compileTypes
			.assertEqualTo(stack())
	}

	@Test
	fun struct() {
		script(
			line(literal(10)),
			line(literal("foo")))
			.compileTypes
			.assertEqualTo(stack(numberType, textType))
	}
}
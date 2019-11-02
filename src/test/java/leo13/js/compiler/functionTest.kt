package leo13.js.compiler

import leo.base.assertEqualTo
import leo13.base.linesString
import org.junit.Test

class FunctionTest {
	@Test
	fun code() {
		Function(emptyTypes, expression("jajeczko").then(expression(argument)) of emptyTypes)
			.code(12)
			.assertEqualTo(
				linesString(
					"function fn12(it) {",
					"  \"jajeczko\"; return it",
					"}")
			)
	}
}
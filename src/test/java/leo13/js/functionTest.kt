package leo13.js

import leo.base.assertEqualTo
import leo13.base.linesString
import org.junit.Test

class FunctionTest {
	@Test
	fun code() {
		Function(emptyType, emptyType, expression("jajeczko").then(expression(argument)))
			.code(12)
			.assertEqualTo(
				linesString(
					"function fn12(it) {",
					"  \"jajeczko\"; return it",
					"}")
			)
	}
}
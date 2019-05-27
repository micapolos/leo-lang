package leo4

import leo.base.assertEqualTo
import kotlin.test.Test

class TermTest {
	@Test
	fun construct() {
		script(
			term(
				line("two"),
				line("plus", script(term(line("two"))))))
			.code
			.assertEqualTo("two()plus(two())")
	}
}
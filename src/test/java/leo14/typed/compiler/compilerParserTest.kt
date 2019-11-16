package leo14.typed.compiler

import leo.base.assertEqualTo
import kotlin.test.Test

class CompilerParserTest {
	@Test
	fun test() {
		compile("zero()")
			.assertEqualTo(null)
	}
}
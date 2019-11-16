package leo14.typed.compiler

import leo.base.assertEqualTo
import leo14.typed.typed
import kotlin.test.Test

class CompilerParserTest {
	@Test
	fun test() {
		compile("zero()")
			.assertEqualTo(typed("zero"))
	}
}
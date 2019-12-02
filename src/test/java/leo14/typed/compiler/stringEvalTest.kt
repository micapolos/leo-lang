package leo14.typed.compiler

import leo.base.assertEqualTo
import leo14.Keyword
import leo14.string
import leo14.typed.compiler.natives.leoEval
import kotlin.test.Test

class StringEvalTest {
	@Test
	fun test() {
		"point x zero   y one    "
			.leoEval
			.assertEqualTo("point(x(zero())y(one()))")

		"${Keyword.DEFINE.string} x  is y    x  "
			.leoEval
			.assertEqualTo("y()")
	}
}
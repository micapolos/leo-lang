package leo14.typed.compiler

import leo.base.assertEqualTo
import kotlin.test.Test

class StringEvalTest {
	@Test
	fun test() {
		"point x zero   y one    "
			.leoEval
			.assertEqualTo("point(x(zero())y(one())))")

		"remember x  is y    x  "
			.leoEval
			.assertEqualTo("y()")
	}
}
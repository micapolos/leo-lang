package leo13

import leo.base.assertEqualTo
import kotlin.test.Test

class EvalTest {
	@Test
	fun eval() {
		"vec(x(zero())y(one()))x()".eval.assertEqualTo("x(zero())")
	}
}
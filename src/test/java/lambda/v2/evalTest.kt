package lambda.v2

import leo.base.assertEqualTo
import kotlin.test.Test

class EvalTest {
	@Test
	fun eval() {
		val id = fn { arg(0) }
		val zero = fn { fn { arg(0) } }
		val one = fn { fn { arg(1) } }

		id.eval.assertEqualTo(id)
		zero.eval.assertEqualTo(zero)
		one.eval.assertEqualTo(one)
		id(zero).assertEqualTo(zero)
		id(one).assertEqualTo(one)
	}
}
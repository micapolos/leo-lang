package lambda.v2

import leo.base.assertEqualTo
import kotlin.test.Test

class EvalTest {
	@Test
	fun eval() {
		val id = fn(1) { arg(1) }
		val zero = fn(2) { arg(1) }
		val one = fn(2) { arg(2) }

		id.eval.assertEqualTo(id)
		zero.eval.assertEqualTo(zero)
		one.eval.assertEqualTo(one)
		id(zero).assertEqualTo(zero)
		id(one).assertEqualTo(one)
	}
}
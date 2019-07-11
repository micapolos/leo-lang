package lambda.v2

import leo.base.assertEqualTo
import kotlin.test.Test

class EvalTest {
	@Test
	fun eval() {
		val id = fn(1) { arg.term }
		val zero = fn(2) { arg.prev.term }
		val one = fn(2) { arg.term }

		id.eval.assertEqualTo(id)
		zero.eval.assertEqualTo(zero)
		one.eval.assertEqualTo(one)
		id(zero).eval.assertEqualTo(zero)
		id(one).eval.assertEqualTo(one)
	}
}
package leo14.lambda2

import leo.base.assertEqualTo
import leo14.untyped.typed.asInt
import kotlin.test.Test
import kotlin.test.assertFails

class EvalTest {
	@Test
	fun evaluatesOnce() {
		val term = 123.valueTerm.assertEvaluatesOnce
		term.eval.assertEqualTo(123.valueTerm)
		assertFails { term.eval }
	}

	@Test
	fun constant() {
		value(0).eval.assertEqualTo(value(0))
	}

	@Test
	fun unbound() {
		assertFails { at(0).eval }
	}

	@Test
	fun get() {
		fn(at(0)).invoke(value(123)).eval.assertEqualTo(value(123))
		fn(fn(at(0))).invoke(value(123)).invoke(value(124)).eval.assertEqualTo(value(124))
		fn(fn(at(1))).invoke(value(123)).invoke(value(124)).eval.assertEqualTo(value(123))
	}

	@Test
	fun func() {
		fn { it }
			.invoke(value(2))
			.eval
			.assertEqualTo(value(2))

		fn { lhs ->
			fn { rhs ->
				value(lhs.value.asInt - rhs.value.asInt)
			}
		}
			.invoke(value(5))
			.invoke(value(3))
			.eval
			.assertEqualTo(value(2))
	}
}
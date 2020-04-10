package leo14.lambda2

import leo.base.assertEqualTo
import leo14.untyped.typed.asInt
import kotlin.test.Test
import kotlin.test.assertFails

class EvalTest {
	@Test
	fun constant() {
		term(0).eval.assertEqualTo(term(0))
	}

	@Test
	fun unbound() {
		assertFails { at(0).eval }
	}

	@Test
	fun get() {
		fn(at(0)).invoke(term(123)).eval.assertEqualTo(term(123))
		fn(fn(at(0))).invoke(term(123)).invoke(term(124)).eval.assertEqualTo(term(124))
		fn(fn(at(1))).invoke(term(123)).invoke(term(124)).eval.assertEqualTo(term(123))
	}

	@Test
	fun func() {
		fn { it }
			.invoke(term(2))
			.eval
			.assertEqualTo(term(2))

		fn { lhs ->
			fn { rhs ->
				term(lhs.value.asInt - rhs.value.asInt)
			}
		}
			.invoke(term(5))
			.invoke(term(3))
			.eval
			.assertEqualTo(term(2))
	}
}
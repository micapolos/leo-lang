package leo15.lambda

import leo.base.assertEqualTo
import leo.base.assertTimesOutMillis
import leo14.untyped.typed.asInt
import leo15.terms.ifIntZero
import leo15.terms.intMinus
import leo15.terms.otherwise
import leo15.terms.term
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
	fun closure_depth1() {
		fn(fn(at(1)))
			.invoke("foo".valueTerm)
			.eval
			.assertEqualTo(fn(fn(at(1))).invoke("foo".valueTerm))
	}

	@Test
	fun closure_depth2() {
		fn(fn(fn(at(2))))
			.invoke("foo".valueTerm)
			.invoke("bar".valueTerm)
			.eval
			.assertEqualTo(
				fn(fn(fn(at(2))))
					.invoke("foo".valueTerm)
					.invoke("bar".valueTerm))
	}

	@Test
	fun closure_resolved() {
		fn(fn(at(1)))
			.invoke("foo".valueTerm)
			.invoke("bar".valueTerm)
			.eval
			.assertEqualTo("foo".valueTerm)
	}

	@Test
	fun func() {
		termFn { it }
			.invoke(value(2))
			.eval
			.assertEqualTo(value(2))

		termFn { lhs ->
			termFn { rhs ->
				value(lhs.value.asInt - rhs.value.asInt)
			}
		}
			.invoke(value(5))
			.invoke(value(3))
			.eval
			.assertEqualTo(value(2))
	}

	@Test
	fun pairAndUnpairAndRePair() {
		fn(fn(fn(pairTerm(at(0))(at(1))))
			.invoke(at(0).invoke(firstTerm))
			.invoke(at(0).invoke(secondTerm)))
			.invoke(pairTerm.invoke("one".valueTerm).invoke("two".valueTerm))
			.eval
			.assertEqualTo(pairTerm("two".valueTerm)("one".valueTerm))
	}

	@Test
	fun infiniteLoop() {
		assertTimesOutMillis(100L) {
			lambda { f ->
				f.invoke(f)
			}.invoke(
				lambda { f ->
					f.invoke(f)
				})
				.eval
				.assertEqualTo(null)
		}
	}

	@Test
	fun tailRecursion_zero() {
		lambda { f ->
			f.invoke(f)
		}.invoke(
			lambda { f ->
				lambda { x ->
					x.ifIntZero { x }.otherwise { f.invoke(f).invoke(x.intMinus(1.term)) }
				}
			})
			.invoke(0.term)
			.eval
			.assertEqualTo(0.term)
	}

	@Test
	fun tailRecursion_recurseOnce() {
		lambda { f ->
			lambda { x ->
				f.invoke(f).invoke(x)
			}
		}.invoke(
			lambda { f ->
				lambda { x ->
					x.ifIntZero { x }.otherwise { f.invoke(f).invoke(x.intMinus(1.term)) }
				}
			})
			.invoke(1.term)
			.eval
			.assertEqualTo(0.term)
	}

	@Test
	fun tailRecursion_recurseSmall() {
		lambda { f ->
			lambda { x ->
				f.invoke(f).invoke(x)
			}
		}.invoke(
			lambda { f ->
				lambda { x ->
					x.ifIntZero { x }.otherwise { f.invoke(f).invoke(x.intMinus(1.term)) }
				}
			})
			.invoke(100.term)
			.eval
			.assertEqualTo(0.term)
	}

	@Test
	fun tailRecursion_recurseHuge() {
		lambda { f ->
			lambda { x ->
				f.invoke(f).invoke(x)
			}
		}.invoke(
			lambda { f ->
				lambda { x ->
					x.ifIntZero { x }.otherwise { f.invoke(f).invoke(x.intMinus(1.term)) }
				}
			})
			.invoke(1000000.term)
			.eval
			.assertEqualTo(0.term)
	}
}
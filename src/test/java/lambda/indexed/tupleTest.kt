package lambda.indexed

import leo.base.assertEqualTo
import leo.base.nat
import kotlin.test.Test

class TupleTest {
	@Test
	fun all() {
		tuple(2).assertEqualTo(fn(3) { arg(3)(arg(1), arg(2)) })

		tuple(10.nat.term, 20.nat.term).tupleAt(nthOf(1, 2)).eval.assertEqualTo(10.nat.term)
		tuple(10.nat.term, 20.nat.term).tupleAt(nthOf(2, 2)).eval.assertEqualTo(20.nat.term)

		nthOf(1, 2).eval.assertEqualTo(fn(2) { arg(1) })
		nthOf(2, 2).eval.assertEqualTo(fn(2) { arg(2) })
	}
}
package lambda.v2

import leo.base.assertEqualTo
import leo.base.nat
import kotlin.test.Test

class TupleTest {
	@Test
	fun all() {
		tuple(2).assertEqualTo(fn(3) { arg(3)(arg(1), arg(2)) })

		tuple(2)(10.nat.term, 20.nat.term)(nthOf(1, 2)).assertEqualTo(10.nat.term)
		tuple(2)(10.nat.term, 20.nat.term)(nthOf(2, 2)).assertEqualTo(20.nat.term)

		nthOf(1, 2).assertEqualTo(fn(2) { arg(1) })
		nthOf(2, 2).assertEqualTo(fn(2) { arg(2) })
	}
}
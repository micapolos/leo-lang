package lambda.indexed

import leo.base.assertEqualTo
import leo.base.nat
import kotlin.test.Test

class PairTest {
	@Test
	fun test() {
		pair(1.nat.term, 2.nat.term).pairFirst.eval.assertEqualTo(1.nat.term)
		pair(1.nat.term, 2.nat.term).pairSecond.eval.assertEqualTo(2.nat.term)
	}
}
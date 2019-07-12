package lambda.indexed

import leo.base.assertEqualTo
import org.junit.Test

class NatTest {
	@Test
	fun test() {
		zero.forZeroOrPred(id, id).eval.assertEqualTo(id)
		zero.succ.forZeroOrPred(id, id).eval.assertEqualTo(zero)
		zero.succ.succ.forZeroOrPred(id, id).eval.assertEqualTo(zero.succ)
	}
}
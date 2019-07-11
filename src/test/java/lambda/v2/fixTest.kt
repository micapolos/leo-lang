package lambda.v2

import leo.base.assertEqualTo
import leo.base.string
import org.junit.Test

class FixTest {
	@Test
	fun test() {
		fix.string.assertEqualTo("fn { fn { a1(a0(a0)) }(fn { a1(a0(a0)) }) }")

		val natCollapse = fix(fn { fn { arg.term.forZeroOrPred(id, arg.prev.term) } })
		natCollapse(zero).eval.assertEqualTo(id)
		natCollapse(zero.succ).eval.assertEqualTo(id)
		natCollapse(zero.succ.succ).eval.assertEqualTo(id)

		val accNatRebuild = fix(fn {
			fn {
				a0.pairSecond.forZeroOrPred(fn { a1.pairFirst }, fn { a2(pair(succ(a1.pairFirst), a0)) })
			}
		})
		accNatRebuild(pair(zero, zero)).eval.assertEqualTo(id)
		accNatRebuild(pair(zero, zero.succ)).eval.assertEqualTo(zero.succ)
		accNatRebuild(pair(zero, zero.succ.succ)).eval.assertEqualTo(zero.succ.succ)
	}
}
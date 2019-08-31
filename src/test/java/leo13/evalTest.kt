package leo13

import leo.base.assertEqualTo
import kotlin.test.Test

class EvalTest {
	@Test
	fun eval() {
		"vec(x(zero())y(one()))x()".eval.assertEqualTo("x(zero())")
		"vec(x(zero())y(one()))y()".eval.assertEqualTo("y(one())")
		"zero()gives(one())zero()".eval.assertEqualTo("one()")

		"zero()of(zero()or(one()))switch(zero()gives(foo()of(foo()or(bar())))one()gives(bar()of(foo()or(bar()))))".eval.assertEqualTo("foo()")
		"one()of(zero()or(one()))switch(zero()gives(foo()of(foo()or(bar())))one()gives(bar()of(foo()or(bar()))))".eval.assertEqualTo("bar()")

		"zero()or(one())exists()zero()switch(zero()gives(one())one()gives(zero()))".eval.assertEqualTo("one()")
		"zero()or(one())exists()one()switch(zero()gives(one())one()gives(zero()))".eval.assertEqualTo("zero()")
	}
}
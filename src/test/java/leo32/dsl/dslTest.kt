package leo32.dsl

import leo.base.assertEqualTo
import leo32.runtime.expr
import leo32.runtime.invoke
import kotlin.test.Test

class DslTest {
	@Test
	fun invoke_bypass() {
		invoke(zero()).assertEqualTo(expr(zero()))
		invoke(bit(zero())).assertEqualTo(expr(bit(zero())))
		invoke(x(zero()), y(zero())).assertEqualTo(expr(x(zero()), y(zero())))
		invoke(vec(x(zero()), y(zero()))).assertEqualTo(expr(vec(x(zero()), y(zero()))))
	}

	@Test
	fun invoke_wrap() {
		invoke(zero(), bit()).assertEqualTo(expr(bit(zero())))
		invoke(zero(), bit(), not()).assertEqualTo(expr(not(bit(zero()))))
	}

	@Test
	fun invoke_at() {
		invoke(vec(x(zero()), y(one())), vec()).assertEqualTo(expr(x(zero()), y(one())))
		invoke(vec(x(zero()), y(one())), vec(), x()).assertEqualTo(expr(zero()))
		invoke(vec(x(zero()), y(one())), vec(), y()).assertEqualTo(expr(one()))
		invoke(vec(x(zero()), y(one())), vec(), x(), zero()).assertEqualTo(expr())
		invoke(vec(x(zero()), y(one())), vec(), y(), one()).assertEqualTo(expr())
		invoke(vec(x(zero()), y(one())), vec(), center()).assertEqualTo(expr(center(x(zero()), y(one()))))
	}
}
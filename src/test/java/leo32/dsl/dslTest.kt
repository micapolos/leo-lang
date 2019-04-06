package leo32.dsl

import leo.base.assertEqualTo
import leo.base.empty
import leo32.runtime.*
import kotlin.test.Test

class DslTest {
	@Test
	fun dsl() {
		val scope = empty.scope
			.define(term("not" to term("zero")) to function("one"))
			.define(term("not" to term("one")) to function("zero"))

		scope.eval(zero()).assertEqualTo(term(zero()))
		scope.eval(not(zero())).assertEqualTo(term(one()))
		scope.eval(not(one())).assertEqualTo(term(zero()))
		scope.eval(not(not(zero()))).assertEqualTo(term(zero()))
	}

	@Test
	fun invoke_bypass() {
		invoke().assertEqualTo(term())
		invoke(zero()).assertEqualTo(term(zero()))
		invoke(bit(zero())).assertEqualTo(term(bit(zero())))
		invoke(x(zero()), y(zero())).assertEqualTo(term(x(zero()), y(zero())))
		invoke(vec(x(zero()), y(zero()))).assertEqualTo(term(vec(x(zero()), y(zero()))))
	}

	@Test
	fun invoke_wrap() {
		invoke(zero(), bit()).assertEqualTo(term(bit(zero())))
		invoke(zero(), bit(), not()).assertEqualTo(term(not(bit(zero()))))
	}

	@Test
	fun invoke_at() {
		invoke(vec(x(zero()), y(one())), vec()).assertEqualTo(term(x(zero()), y(one())))
		invoke(vec(x(zero()), y(one())), vec(), x()).assertEqualTo(term(zero()))
		invoke(vec(x(zero()), y(one())), vec(), y()).assertEqualTo(term(one()))
		invoke(vec(x(zero()), y(one())), vec(), x(), zero()).assertEqualTo(term())
		invoke(vec(x(zero()), y(one())), vec(), y(), one()).assertEqualTo(term())
		invoke(vec(x(zero()), y(one())), vec(), center()).assertEqualTo(term(center(x(zero()), y(one()))))
	}
}
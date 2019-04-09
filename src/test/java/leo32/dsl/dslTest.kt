package leo32.dsl

import leo.base.assertEqualTo
import leo32.runtime.Line
import leo32.runtime.Script
import leo32.runtime.boolean
import leo32.runtime.invoke
import kotlin.test.Test

class DslTest {
	@Test
	fun invoke_bypass() {
		_eval(zero()).assertGives(zero())
		_eval(bit(zero())).assertGives(bit(zero()))
		_eval(x(zero()), y(zero())).assertGives(x(zero()), y(zero()))
		_eval(vec(x(zero()), y(zero()))).assertGives(vec(x(zero()), y(zero())))
	}

	@Test
	fun invoke_wrap() {
		_eval(zero(), bit()).assertGives(bit(zero()))
		_eval(zero(), bit(), not()).assertGives(not(bit(zero())))
	}

	@Test
	fun invoke_at() {
		_eval(vec(x(zero()), y(one())), vec()).assertGives(x(zero()), y(one()))
		_eval(vec(x(zero()), y(one())), vec(), x()).assertGives(zero())
		_eval(vec(x(zero()), y(one())), vec(), y()).assertGives(one())
		_eval(vec(x(zero()), y(one())), vec(), x(), zero()).assertGives()
		_eval(vec(x(zero()), y(one())), vec(), y(), one()).assertGives()
		_eval(vec(x(zero()), y(one())), vec(), center()).assertGives(center(x(zero()), y(one())))
	}

	@Test
	fun chaining() {
		_eval(script()).assertGives(script())
		_eval(script(zero())).assertGives(script(zero()))
		_eval(script(zero(), zero())).assertGives(script())
		_eval(script(zero(), zero(), zero())).assertGives(script(zero()))
		_eval(script(zero(), zero(), zero(), zero())).assertGives(script())
	}

	@Test
	fun equality() {
		_eval(script(doesEqual())).assertGives(script(boolean(true)))
		_eval(script(zero(), doesEqual(zero()))).assertGives(script(boolean(true)))
		_eval(script(zero(), doesEqual(one()))).assertGives(script(boolean(false)))
	}

	@Test
	fun quoting() {
		_eval(quote()).assertGives()
		_eval(quote(zero())).assertGives(zero())
		_eval(quote(zero(), zero())).assertGives(zero(), zero())
	}
}

fun Script.assertGives(vararg lines: Line) =
	assertEqualTo(leo32.runtime.script(*lines))
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
		invoke(zero()).assertGives(zero())
		invoke(bit(zero())).assertGives(bit(zero()))
		invoke(x(zero()), y(zero())).assertGives(x(zero()), y(zero()))
		invoke(vec(x(zero()), y(zero()))).assertGives(vec(x(zero()), y(zero())))
	}

	@Test
	fun invoke_wrap() {
		invoke(zero(), bit()).assertGives(bit(zero()))
		invoke(zero(), bit(), not()).assertGives(not(bit(zero())))
	}

	@Test
	fun invoke_at() {
		invoke(vec(x(zero()), y(one())), vec()).assertGives(x(zero()), y(one()))
		invoke(vec(x(zero()), y(one())), vec(), x()).assertGives(zero())
		invoke(vec(x(zero()), y(one())), vec(), y()).assertGives(one())
		invoke(vec(x(zero()), y(one())), vec(), x(), zero()).assertGives()
		invoke(vec(x(zero()), y(one())), vec(), y(), one()).assertGives()
		invoke(vec(x(zero()), y(one())), vec(), center()).assertGives(center(x(zero()), y(one())))
	}

	@Test
	fun chaining() {
		invoke(script()).assertGives(script())
		invoke(script(zero())).assertGives(script(zero()))
		invoke(script(zero(), zero())).assertGives(script())
		invoke(script(zero(), zero(), zero())).assertGives(script(zero()))
		invoke(script(zero(), zero(), zero(), zero())).assertGives(script())
	}

	@Test
	fun equality() {
		invoke(script(doesEqual())).assertGives(script(boolean(true)))
		invoke(script(zero(), doesEqual(zero()))).assertGives(script(boolean(true)))
		invoke(script(zero(), doesEqual(one()))).assertGives(script(boolean(false)))
	}

	@Test
	fun quoting() {
		invoke(script(quote())).assertGives(script())
		invoke(script(quote(zero()))).assertGives(script(zero()))
		invoke(script(quote(zero(), zero()))).assertGives(script())
	}
}

fun Script.assertGives(vararg lines: Line) =
	assertEqualTo(leo32.runtime.script(*lines))
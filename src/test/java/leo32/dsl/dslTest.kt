package leo32.dsl

import leo.base.assertEqualTo
import leo32.runtime.Line
import leo32.runtime.Script
import leo32.runtime.boolean
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
		_eval(quote(one(), two())).assertGives(one(), two())
		_eval(quote(x(zero()), y(one()), x())).assertGives(x(zero()), y(one()), x())
	}

	@Test
	fun aliases() {
		_eval(boolean(true)).assertGives(boolean(_true()))
		_eval(byte(123)).assertGives(byte(_line("123")))
		_eval(short(123)).assertGives(short(_line("123")))
		_eval(int(123)).assertGives(int(_line("123")))
		_eval(long(123)).assertGives(long(_line("123")))
		_eval(float(123f)).assertGives(float(_line("123.0")))
		_eval(double(123.0)).assertGives(double(_line("123.0")))
		_eval(char('a')).assertGives(char(_line("'a'")))
		_eval(string("foo")).assertGives(string(_line("\"foo\"")))
	}

	@Test
	fun switching() {
		_eval(
			negate(bit(zero())),
			switch(
				case(
					negate(bit(zero())),
					_to(bit(one()))),
				case(
					negate(bit(one())),
					_to(bit(zero())))))
			.assertGives(bit(one()))

		_eval(
			negate(bit(one())),
			switch(
				case(
					negate(bit(zero())),
					_to(bit(one()))),
				case(
					negate(bit(one())),
					_to(bit(zero())))))
			.assertGives(bit(zero()))

		_eval(
			negate(bit(two())),
			switch(
				case(
					negate(bit(zero())),
					_to(bit(one()))),
				case(
					negate(bit(one())),
					_to(bit(zero())))))
			.assertGives(
				negate(bit(two())),
				switch(
					case(
						negate(bit(zero())),
						_to(bit(one()))),
					case(
						negate(bit(one())),
						_to(bit(zero())))))
	}
}

fun Script.assertGives(vararg lines: Line) =
	assertEqualTo(leo32.runtime.script(*lines))
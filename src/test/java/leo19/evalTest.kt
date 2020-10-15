package leo19

import leo.base.assertEqualTo
import leo14.Script
import leo14.invoke
import leo14.plus
import leo14.script
import leo16.names.*
import kotlin.test.Test
import kotlin.test.assertFails

fun Script.assertGives(script: Script) = eval.assertEqualTo(script)

class EvalTest {
	@Test
	fun empty() {
		script().assertGives(script())
	}

	@Test
	fun name() {
		script(_zero()).assertGives(script(_zero()))
	}

	@Test
	fun struct() {
		script(
			_x(_zero()),
			_y(_one()))
			.assertGives(
				script(
					_x(_zero()),
					_y(_one())))
	}

	@Test
	fun get0() {
		script(
			_point(_x(_zero()), _y(_one())),
			_x())
			.assertGives(script(_x(_zero())))
	}

	@Test
	fun get1() {
		script(
			_point(_x(_zero()), _y(_one())),
			_y())
			.assertGives(script(_y(_one())))
	}

	@Test
	fun make() {
		script(
			_x(_zero()),
			_y(_one()),
			_point())
			.assertGives(
				script(
					_point(
						_x(_zero()),
						_y(_one()))))
	}

	@Test
	fun choice0() {
		script(
			_bit(
				_choice(
					_yes(_zero()),
					_no(_one()))))
			.assertGives(script(_bit(_zero())))
	}

	@Test
	fun choice1() {
		script(
			_bit(
				_choice(
					_no(_zero()),
					_yes(_one()))))
			.assertGives(script(_bit(_one())))
	}

	@Test
	fun switch0() {
		script(
			_bit(
				_choice(
					_yes(_zero()),
					_no(_one()))),
			_switch(
				_zero(
					_boolean(
						_choice(
							_yes(_false()),
							_no(_true())))),
				_one(
					_boolean(
						_choice(
							_no(_false()),
							_yes(_true()))))))
			.assertGives(script(_boolean(_false())))
	}

	@Test
	fun switch1() {
		script(
			_bit(
				_choice(
					_no(_zero()),
					_yes(_one()))),
			_switch(
				_zero(
					_boolean(
						_choice(
							_yes(_false()),
							_no(_true())))),
				_one(
					_boolean(
						_choice(
							_no(_false()),
							_yes(_true()))))))
			.assertGives(script(_boolean(_true())))
	}

	@Test
	fun switchGiven() {
		script(
			_shape(_choice(_yes(_circle(_radius(_zero()))))),
			_switch(_circle(_radius())))
			.assertGives(script(_radius(_zero())))
	}

	@Test
	fun give_static() {
		script(
			_zero(),
			_do(_one()))
			.assertGives(script(_one()))
	}

	@Test
	fun give_dynamic() {
		script(
			_x(_zero()),
			_y(_one()),
			_do(_x()))
			.assertGives(script(_x(_zero())))
	}

	@Test
	fun defineIs() {
		script(
			_define(
				_zero(),
				_is(_one())))
			.assertGives(script())
	}

	@Test
	fun defineIsResolve() {
		script(
			_define(
				_zero(),
				_is(_one())),
			_zero())
			.assertGives(script(_one()))
	}

	@Test
	fun defineGives() {
		script(
			_define(
				_zero(),
				_does(_one())))
			.assertGives(script())
	}

	@Test
	fun defineGivesResolve() {
		script(
			_define(
				_bit(_zero()),
				_does(_done(_bit()))),
			_bit(_zero()))
			.assertGives(script(_done(_bit(_zero()))))
	}

	@Test
	fun equals_true() {
		script(
			_choice(_yes(_zero()), _no(_one())),
			_equals(_choice(_yes(_zero()), _no(_one()))))
			.assertGives(script(_equals(_boolean(_true()))))
	}

	@Test
	fun equals_false() {
		script(
			_choice(_yes(_zero()), _no(_one())),
			_equals(_choice(_no(_zero()), _yes(_one()))))
			.assertGives(script(_equals(_boolean(_false()))))
	}

	@Test
	fun test_match() {
		script(
			_test(true.script.plus(_equals(true.script))))
			.assertGives(script())
	}

	@Test
	fun test_mismatch() {
		assertFails {
			script(_test(true.script.plus(_equals(false.script)))).eval
		}
	}
}
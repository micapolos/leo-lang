package leo19

import leo.base.assertEqualTo
import leo14.Script
import leo14.invoke
import leo14.script
import leo16.names.*
import kotlin.test.Test

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
	fun give() {
		script(
			_zero(),
			_give(_one()))
			.assertGives(script(_one()))
	}


	@Test
	fun giveGiven() {
		script(
			_zero(),
			_give(_given()))
			.assertGives(script(_given(_zero())))
	}
}
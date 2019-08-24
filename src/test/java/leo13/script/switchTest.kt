package leo13.script

import leo.base.assertEqualTo
import leo13.lineTo
import leo13.script
import kotlin.test.Test

class SwitchTest {
	private val evalSwitch =
		switch(
			"one" caseTo expr(op("jeden" lineTo expr())),
			"two" caseTo expr(op("dwa" lineTo expr())))

	@Test
	fun eval_first() {
		evalSwitch
			.eval(bindings(), script("one" lineTo script("rhs" lineTo script())))
			.assertEqualTo(
				script(
					"rhs" lineTo script(),
					"jeden" lineTo script()))
	}

	@Test
	fun eval_second() {
		evalSwitch
			.eval(bindings(), script("two" lineTo script("rhs" lineTo script())))
			.assertEqualTo(
				script(
					"rhs" lineTo script(),
					"dwa" lineTo script()))
	}
}
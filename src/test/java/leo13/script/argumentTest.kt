package leo13.script

import leo.base.assertEqualTo
import leo13.argument
import leo13.lineTo
import leo13.previous
import leo13.script
import kotlin.test.Test

class ArgumentTest {
	private val evalBindings = bindings(
		script("one" lineTo script()),
		script("two" lineTo script()),
		script("three" lineTo script()))

	@Test
	fun eval_this() {
		argument()
			.eval(evalBindings, script("lhs" lineTo script()))
			.assertEqualTo(script("three" lineTo script()))
	}

	@Test
	fun eval_previous() {
		argument(previous)
			.eval(evalBindings, script("lhs" lineTo script()))
			.assertEqualTo(script("two" lineTo script()))
	}

	@Test
	fun eval_previous_previous() {
		argument(previous, previous)
			.eval(evalBindings, script("lhs" lineTo script()))
			.assertEqualTo(script("one" lineTo script()))
	}
}
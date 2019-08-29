package leo13.interpreter

import leo.base.assertEqualTo
import leo13.compiler.context
import leo13.script.lineTo
import leo13.script.script
import leo13.type.lineTo
import leo13.type.type
import leo13.value.lineTo
import leo13.value.value
import kotlin.test.Test

class InterpreterTest {
	@Test
	fun all() {
		interpreter()
			.unsafePush(
				script(
					"rect" lineTo script("color" lineTo script("red" lineTo script())),
					"color" lineTo script()))
			.assertEqualTo(
				interpreter(
					context(),
					value("color" lineTo value("red" lineTo value())),
					type("color" lineTo type("red" lineTo type()))))
	}
}
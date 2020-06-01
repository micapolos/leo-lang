package leo16

import leo.base.assertEqualTo
import leo14.bigDecimal
import leo14.literal
import leo14.script
import kotlin.test.Test

class ScriptValueTest {
	@Test
	fun listScriptValue_empty() {
		script(literal("Hello, world!"))
			.asValue
			.assertEqualTo(value("Hello, world!".sentence))

		script(literal(123))
			.asValue
			.assertEqualTo(value(123.bigDecimal.sentence))
	}
}
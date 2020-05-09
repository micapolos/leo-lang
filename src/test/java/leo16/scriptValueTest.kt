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
			.assertEqualTo("Hello, world!".field.value)

		script(literal(123))
			.asValue
			.assertEqualTo(123.bigDecimal.field.value)
	}
}
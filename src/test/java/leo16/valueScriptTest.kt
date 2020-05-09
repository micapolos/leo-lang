package leo16

import leo.base.assertEqualTo
import leo13.stack
import leo14.lineTo
import leo14.script
import leo16.names.*
import kotlin.test.Test

class ValueScriptTest {
	@Test
	fun valueListScript_empty() {
		stack<Value>()
			.field(_stack)
			.listScriptLineOrNull
			.assertEqualTo(
				_stack lineTo script(_empty))
	}

	@Test
	fun valueListScript_linked() {
		stack(value(_zero()), value(_one()))
			.field(_stack)
			.listScriptLineOrNull
			.assertEqualTo(
				_stack lineTo script(
					_item lineTo script(_zero),
					_item lineTo script(_one)))
	}
}
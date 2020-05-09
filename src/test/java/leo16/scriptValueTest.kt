package leo16

import leo.base.assertEqualTo
import leo16.names.*
import kotlin.test.Test
import leo14.invoke as to

class ScriptValueTest {
	@Test
	fun listScriptValue_empty() {
		_stack.to(_empty.to())
			.field
			.assertEqualTo(
				_stack(_empty())
			)
	}

	@Test
	fun listScriptValue_nonEmpty() {
		_stack.to(
			_item.to(_zero.to()),
			_item.to(_one.to()))
			.field
			.assertEqualTo(
				_stack(_linked(
					_previous(_stack(_linked(
						_previous(_stack(_empty())),
						_last(_item(_zero()))))),
					_last(_item(_one())))))
	}
}
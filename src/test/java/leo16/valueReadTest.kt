package leo16

import leo.base.assertEqualTo
import leo16.names.*
import kotlin.test.Test

class ValueReadTest {
	@Test
	fun listScriptValue_empty() {
		_stack(_empty())
			.listFieldOrNull
			.assertEqualTo(null)
	}

	@Test
	fun listScriptValue_nonEmpty() {
		_stack(
			_item(_zero()),
			_item(_one()))
			.listFieldOrNull
			.assertEqualTo(
				_stack(_linked(
					_previous(_stack(_linked(
						_previous(_stack(_empty())),
						_last(_zero())))),
					_last(_one()))))
	}
}
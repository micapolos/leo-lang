package leo16

import leo.base.assertEqualTo
import leo16.names.*
import kotlin.test.Test

class ValuePrintTest {
	@Test
	fun valueListScript_empty() {
		_stack(_empty())
			.listPrintOrNull
			.assertEqualTo(_stack(_empty()))
	}

	@Test
	fun valueListScript_linked() {
		_stack(_list(
			_previous(_stack(_list(
				_previous(_stack(_empty())),
				_last(_zero())))),
			_last(_one())))
			.listPrintOrNull
			.assertEqualTo(
				_stack(
					_item(_zero()),
					_item(_one())))
	}
}
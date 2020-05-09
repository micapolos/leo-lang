package leo16

import leo.base.assertEqualTo
import leo16.names.*
import kotlin.test.Test

class ValueReadTest {
	@Test
	fun readEmpty_empty() {
		_stack(_empty())
			.read
			.assertEqualTo(_stack(_empty()))
	}

	@Test
	fun readList_nonEmpty() {
		_stack(
			_next(_zero()),
			_next(_one()))
			.read
			.assertEqualTo(
				_stack(_linked(
					_previous(_stack(_linked(
						_previous(_stack(_empty())),
						_last(_zero())))),
					_last(_one()))))
	}
}
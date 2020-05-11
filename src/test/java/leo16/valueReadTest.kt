package leo16

import leo.base.assertEqualTo
import leo16.names.*
import kotlin.test.Test

class ValueReadTest {
	@Test
	fun readEmpty_empty() {
		_list(_empty())
			.read
			.assertEqualTo(_list(_empty()))
	}

	@Test
	fun readList_nonEmpty() {
		_list(
			_item(_zero()),
			_item(_one()))
			.read
			.assertEqualTo(
				_list(_linked(
					_previous(_list(_linked(
						_previous(_list(_empty())),
						_last(_zero())))),
					_last(_one()))))
	}
}
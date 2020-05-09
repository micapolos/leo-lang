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
				_list(_link(
					_previous(_list(_link(
						_previous(_list(_empty())),
						_last(_item(_zero()))))),
					_last(_item(_one())))))
	}
}
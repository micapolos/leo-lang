package leo16

import leo.base.assertEqualTo
import leo16.names.*
import kotlin.test.Test

class ValuePrintTest {
	@Test
	fun empty() {
		_list(_empty())
			.printed
			.assertEqualTo(_list(_empty()))
	}

	@Test
	fun list() {
		_list(_link(
			_previous(_list(_link(
				_previous(_list(_empty())),
				_last(_zero())))),
			_last(_one())))
			.printed
			.assertEqualTo(
				_list(
					_item(_zero()),
					_item(_one())))
	}

	@Test
	fun link() {
		_link(
			_previous(_list(_link(
				_previous(_list(_empty())),
				_last(_zero())))),
			_last(_one()))
			.printed
			.assertEqualTo(
				_link(
					_previous(_list(_item(_zero()))),
					_last(_one())))
	}
}
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
		_list(_linked(
			_previous(_list(_linked(
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
	fun linked() {
		_linked(
			_previous(_list(_linked(
				_previous(_list(_empty())),
				_last(_zero())))),
			_last(_one()))
			.printed
			.assertEqualTo(
				_linked(
					_previous(_list(_item(_zero()))),
					_last(_one())))
	}
}
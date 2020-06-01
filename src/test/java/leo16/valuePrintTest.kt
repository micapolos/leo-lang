package leo16

import leo.base.assertEqualTo
import leo16.names.*
import kotlin.test.Test

class ValuePrintTest {
	@Test
	fun empty() {
		value(_list(_empty()))
			.printed
			.assertEqualTo(value(_list(_empty())))
	}

	@Test
	fun list() {
		value(_list(_link(
			_previous(_list(_link(
				_previous(_list(_empty())),
				_last(_zero())))),
			_last(_one()))))
			.printed
			.assertEqualTo(
				value(_list(
					_item(_zero()),
					_item(_one()))))
	}

	@Test
	fun link() {
		value(_link(
			_previous(_list(_link(
				_previous(_list(_empty())),
				_last(_zero())))),
			_last(_one())))
			.printed
			.assertEqualTo(
				value(
					_link(
						_previous(_list(_item(_zero()))),
						_last(_one()))))
	}
}
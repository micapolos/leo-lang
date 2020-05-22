package leo16

import leo13.assertContains
import leo16.names.*
import kotlin.test.Test

class ValueParseTest {
	@Test
	fun valueStackOrNull_empty() {
		_list(_empty())
			.stackOrNull!!
			.assertContains()
	}

	@Test
	fun stackOrNull_link() {
		_list(_link(
			_previous(_list(_link(
				_previous(_list(_empty())),
				_last(_zero())))),
			_last(_one())))
			.stackOrNull!!
			.assertContains(value(_zero()), value(_one()))
	}
}
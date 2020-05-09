package leo16

import leo13.assertContains
import leo16.names.*
import kotlin.test.Test

class ValueParseTest {
	@Test
	fun valueStackOrNull_empty() {
		_stack(_empty())
			.valueStackOrNull!!
			.assertContains()
	}

	@Test
	fun stackOrNull_linked() {
		_stack(_link(
			_previous(_stack(_link(
				_previous(_stack(_empty())),
				_last(_zero())))),
			_last(_one())))
			.valueStackOrNull!!
			.assertContains(value(_zero()), value(_one()))
	}
}
package leo16

import leo.base.assertEqualTo
import leo16.names.*
import kotlin.test.Test

class ValuePrintTest {
	@Test
	fun empty() {
		_stack(_empty())
			.printed
			.assertEqualTo(_stack(_empty()))
	}

	@Test
	fun list() {
		_stack(_link(
			_previous(_stack(_link(
				_previous(_stack(_empty())),
				_last(_zero())))),
			_last(_one())))
			.printed
			.assertEqualTo(
				_stack(
					_item(_zero()),
					_item(_one())))
	}

	@Test
	fun linked() {
		_link(
			_previous(_stack(_link(
				_previous(_stack(_empty())),
				_last(_zero())))),
			_last(_one()))
			.printed
			.assertEqualTo(
				_link(
					_previous(_stack(_item(_zero()))),
					_last(_one())))
	}
}
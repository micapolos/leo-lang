package leo16

import leo.base.assertEqualTo
import leo14.lineTo
import leo14.script
import leo16.names.*
import kotlin.test.Test

class ValueScriptTest {
	@Test
	fun list() {
		value(
			_list(
				_item(_zero()),
				_item(_one())))
			.script
			.assertEqualTo(
				script(
					_list.lineTo(script(
						_item.lineTo(script(_zero)),
						_item.lineTo(script(_one))))))
	}
}
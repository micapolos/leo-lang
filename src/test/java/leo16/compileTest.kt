package leo16

import leo.base.assertEqualTo
import leo16.names.*
import kotlin.test.Test

class CompileTest {
	@Test
	fun list() {
		emptyDictionary
			.compile(
				value(
					_list(
						_item(_zero()),
						_item(_one()))))
			.value
			.assertEqualTo(
				value(
					_list(_link(
						_previous(_list(_link(
							_previous(_list(_empty())),
							_last(_zero())))),
						_last(_one())))))
	}
}
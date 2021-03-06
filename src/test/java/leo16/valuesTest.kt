package leo16

import leo.base.assertEqualTo
import leo13.stack
import leo16.names.*
import kotlin.test.Test

class ValuesTest {
	@Test
	fun emptyStackValue() {
		stack<Value>()
			.valueValue
			.assertEqualTo(value(_list(_empty())))
	}

	@Test
	fun linkStackValue() {
		stack(value(_zero()), value(_one()))
			.valueValue
			.assertEqualTo(
				value(_list(_link(
					_previous(_list(_link(
						_previous(_list(_empty())),
						_last(_zero())))),
					_last(_one())))))
	}
}
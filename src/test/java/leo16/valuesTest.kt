package leo16

import leo.base.assertEqualTo
import leo13.stack
import leo16.names.*
import kotlin.test.Test

class ValuesTest {
	@Test
	fun emptyStackValue() {
		stack<Value>()
			.value(_stack)
			.assertEqualTo(value(_stack(_empty())))
	}

	@Test
	fun linkedStackValue() {
		stack(value(_zero()), value(_one()))
			.value(_stack)
			.assertEqualTo(
				value(_stack(_link(
					_previous(_stack(_link(
						_previous(_stack(_empty())),
						_last(_zero())))),
					_last(_one())))))
	}
}
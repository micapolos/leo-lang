package leo32.term

import leo.base.assertEqualTo
import leo.base.string
import kotlin.test.Test

class TermTest {
	@Test
	fun term() {
		term("two")
			.invoke("plus" fieldTo term("two"))
			.invoke("times" fieldTo term("three"))
			.string
			.assertEqualTo(null)
	}
}
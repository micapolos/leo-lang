package leo32.term

import leo.base.assertEqualTo
import leo32.base.i32
import kotlin.test.Test

class GetterTest {
	@Test
	fun invoke() {
		val term = term(
			"foo" fieldTo term("foo1"),
			"bar" fieldTo term("bar1"))

		term.invoke("foo".getter).assertEqualTo(term("foo1"))
		term.invoke("bar".getter).assertEqualTo(term("bar1"))
		term.invoke(0.i32.getter).assertEqualTo(term("foo1"))
		term.invoke(1.i32.getter).assertEqualTo(term("bar1"))
	}
}
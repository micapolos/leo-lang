package leo32.runtime

import leo.base.assertEqualTo
import leo32.base.i32
import kotlin.test.Test

class GetTest {
	@Test
	fun invoke() {
		val term = term(
			"foo" to term("foo1"),
			"bar" to term("bar1"))

		getter("foo").invoke(term).assertEqualTo(term("foo1"))
		getter("bar").invoke(term).assertEqualTo(term("bar1"))
		getter(0.i32).invoke(term).assertEqualTo(term("foo1"))
		getter(1.i32).invoke(term).assertEqualTo(term("bar1"))
	}
}
package leo32.runtime

import leo.base.assertEqualTo
import leo32.base.i32
import kotlin.test.Test

class GetterTest {
	@Test
	fun invoke() {
		val term = term(
			"foo" to term("foo1"),
			"bar" to term("bar1"))

		"foo".getter.invoke(term).assertEqualTo(term("foo1"))
		"bar".getter.invoke(term).assertEqualTo(term("bar1"))
		0.i32.getter.invoke(term).assertEqualTo(term("foo1"))
		1.i32.getter.invoke(term).assertEqualTo(term("bar1"))
	}
}
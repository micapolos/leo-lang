package leo32.runtime

import leo.base.assertEqualTo
import leo32.base.i32
import kotlin.test.Test

class GetTest {
	@Test
	fun invoke() {
		val term = term(
			"one" to term("1"),
			"two" to term("2"),
			"three" to term("3"))

		get(0.i32).invoke(term).assertEqualTo(term("1"))
		get(1.i32).invoke(term).assertEqualTo(term("2"))
		get(2.i32).invoke(term).assertEqualTo(term("3"))
		get(lhs).invoke(term).assertEqualTo(term("one" to term("1"), "two" to term("2")))
		get(rhs).invoke(term).assertEqualTo(term("3"))
	}
}
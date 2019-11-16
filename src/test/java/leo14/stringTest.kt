package leo14

import leo.base.assertEqualTo
import org.junit.Test

class StringTest {
	@Test
	fun eval() {
		"".eval.assertEqualTo("")
		"foo()".eval.assertEqualTo("foo()")
		"remember(it(zero()) is(one())) zero()".eval.assertEqualTo("one()")
		"2 plus(3)".eval.assertEqualTo("5")
	}
}
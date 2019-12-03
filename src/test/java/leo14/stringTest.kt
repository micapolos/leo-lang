package leo14

import leo.base.assertEqualTo
import org.junit.Test

class StringTest {
	@Test
	fun eval() {
		"".eval.assertEqualTo("")
		"foo  ".eval.assertEqualTo("foo()")
		"define zero  is one    zero  ".eval.assertEqualTo("one()")
		//"2plus(3)".eval.assertEqualTo("5")
	}
}
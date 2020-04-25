package leo15.lambda.java

import leo.base.assertEqualTo
import leo15.lambda.runtime.value
import kotlin.test.Test

class JavaTest {
	@Test
	fun apply() {
		IntPlusIntJava.apply(2.java).value.apply(3.java).value.assertEqualTo(5.java)
		IntMinusIntJava.apply(5.java).value.apply(3.java).value.assertEqualTo(2.java)
		IntTimesIntJava.apply(2.java).value.apply(3.java).value.assertEqualTo(6.java)
		StringPlusStringJava.apply("Hello, ".java).value.apply("world!".java).value.assertEqualTo("Hello, world!".java)
		StringLengthJava.apply("Hello, world!".java).value.assertEqualTo(13.java)
	}
}
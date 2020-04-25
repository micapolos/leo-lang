package leo15.lambda.java

import leo.base.assertEqualTo
import kotlin.test.Test

class JavaTest {
	@Test
	fun apply() {
		IntPlusIntJava.apply(2.java).apply(3.java).assertEqualTo(5.java)
		IntMinusIntJava.apply(5.java).apply(3.java).assertEqualTo(2.java)
		IntTimesIntJava.apply(2.java).apply(3.java).assertEqualTo(6.java)
		StringPlusStringJava.apply("Hello, ".java).apply("world!".java).assertEqualTo("Hello, world!".java)
		StringLengthJava.apply("Hello, world!".java).assertEqualTo(13.java)
	}
}
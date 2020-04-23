package leo15.lambda.java

import leo.base.assertEqualTo
import kotlin.test.Test

class JavaTest {
	@Test
	fun apply() {
		PlusJava.apply(2.java).apply(3.java).assertEqualTo(5.java)
		MinusJava.apply(5.java).apply(3.java).assertEqualTo(2.java)
		TimesJava.apply(2.java).apply(3.java).assertEqualTo(6.java)
		PlusJava.apply("Hello, ".java).apply("world!".java).assertEqualTo("Hello, world!".java)
		LengthJava.apply("Hello, world!".java).assertEqualTo(13.java)
	}
}
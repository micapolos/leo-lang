package leo13.lambda.java

import leo.base.assertEqualTo
import kotlin.test.Test

class RuntimeTest {
	@Test
	fun eval() {
		expr("Hello, world!").eval.assertEqualTo("Hello, world!")
	}
}
package leo14.lambda.java

import leo.base.assertEqualTo
import leo14.lambda.*
import kotlin.test.Test

class EvalTest {
	@Test
	fun helloWorld() {
		term("Hello, world!")
			.eval
			.assertEqualTo("Hello, world!")
	}

	@Test
	fun invoke() {
		fn(arg0)(term("Hello, world!"))
			.eval
			.assertEqualTo("Hello, world!")
	}

	@Test
	fun pairFirst() {
		pair(term("first"), term("second"))
			.first
			.eval
			.assertEqualTo("first")
	}

	@Test
	fun pairSecond() {
		pair(term("first"), term("second"))
			.second
			.eval
			.assertEqualTo("second")
	}
}
package leo13.lambda.java

import leo.base.assertEqualTo
import leo13.lambda.*
import kotlin.test.Test

class EvalTest {
	@Test
	fun helloWorld() {
		value("Hello, world!")
			.eval
			.assertEqualTo("Hello, world!")
	}

	@Test
	fun invoke() {
		fn(arg0)(value("Hello, world!"))
			.eval
			.assertEqualTo("Hello, world!")
	}

	@Test
	fun pairFirst() {
		pair(value("first"), value("second"))
			.first
			.eval
			.assertEqualTo("first")
	}

	@Test
	fun pairSecond() {
		pair(value("first"), value("second"))
			.second
			.eval
			.assertEqualTo("second")
	}
}
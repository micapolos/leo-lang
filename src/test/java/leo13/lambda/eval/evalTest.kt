package leo13.lambda.eval

import leo.base.assertEqualTo
import leo13.lambda.*
import org.junit.Test

class EvalTest {
	@Test
	fun helloWorld() {
		expr("Hello, world!")
			.eval
			.assertEqualTo("Hello, world!")
	}

	@Test
	fun invoke() {
		fn(arg0)(expr("Hello, world!"))
			.eval
			.assertEqualTo("Hello, world!")
	}

	@Test
	fun pairFirst() {
		pair(expr("first"), expr("second"))
			.first
			.eval
			.assertEqualTo("first")
	}

	@Test
	fun pairSecond() {
		pair(expr("first"), expr("second"))
			.second
			.eval
			.assertEqualTo("second")
	}
}
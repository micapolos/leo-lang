package leo13.lambda.java

import leo.base.assertEqualTo
import leo13.lambda.expr.*
import leo13.lambda.java.expr.arg0
import leo13.lambda.java.expr.expr
import kotlin.test.Test

class RuntimeTest {
	@Test
	fun evalHelloWorld() {
		expr("Hello, world!")
			.eval
			.assertEqualTo("Hello, world!")
	}

	@Test
	fun evalFnAp() {
		fn(arg0)(expr("Hello, world!"))
			.eval
			.assertEqualTo("Hello, world!")
	}

	@Test
	fun evalPairFirst() {
		pair(expr("first"), expr("second"))
			.first
			.eval
			.assertEqualTo("first")
	}

	@Test
	fun evalPairSecond() {
		pair(expr("first"), expr("second"))
			.second
			.eval
			.assertEqualTo("second")
	}
}
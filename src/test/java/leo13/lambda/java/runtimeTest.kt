package leo13.lambda.java

import leo.base.assertEqualTo
import leo13.lambda.ap
import leo13.lambda.expr
import leo13.lambda.fn
import kotlin.test.Test

class RuntimeTest {
	@Test
	fun evalHelloWorld() {
		expr(java("Hello, world!")).eval.assertEqualTo("Hello, world!")
	}

	@Test
	fun evalIdentity() {
		expr(
			ap(
				expr(fn(expr(arg))),
				expr(java("Hello, world!"))))
			.eval
			.assertEqualTo("Hello, world!")
	}
}
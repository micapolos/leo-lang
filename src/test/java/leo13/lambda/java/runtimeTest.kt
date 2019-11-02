package leo13.lambda.java

import leo.base.assertEqualTo
import leo13.lambda.ap
import leo13.lambda.fn
import kotlin.test.Test

class RuntimeTest {
	@Test
	fun evalHelloWorld() {
		expr("Hello, world!").eval.assertEqualTo("Hello, world!")
	}

	@Test
	fun evalIdentity() {
		leo13.lambda.expr(ap(
			leo13.lambda.expr(fn(leo13.lambda.expr(arg))),
			expr("Hello, world!")))
			.eval
			.assertEqualTo("Hello, world!")
	}
}
package leo13.lambda.java

import leo.base.assertEqualTo
import leo13.lambda.expr
import leo13.lambda.expr.*
import kotlin.test.Test

class RuntimeTest {
	@Test
	fun evalHelloWorld() {
		expr(java("Hello, world!"))
			.eval
			.assertEqualTo("Hello, world!")
	}

	@Test
	fun evalFnAp() {
		fn(arg<Java>(0))(expr(java("Hello, world!")))
			.eval
			.assertEqualTo("Hello, world!")
	}

	@Test
	fun evalArrowLhs() {
		pair(expr(java("lhs")), expr(java("rhs")))
			.first
			.eval
			.assertEqualTo("lhs")
	}

	@Test
	fun evalArrowRhs() {
		pair(expr(java("lhs")), expr(java("rhs")))
			.second
			.eval
			.assertEqualTo("rhs")
	}
}
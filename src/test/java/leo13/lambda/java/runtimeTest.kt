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
		arrow(expr(java("lhs")), expr(java("rhs")))
			.lhs
			.eval
			.assertEqualTo("lhs")
	}

	@Test
	fun evalArrowRhs() {
		arrow(expr(java("lhs")), expr(java("rhs")))
			.rhs
			.eval
			.assertEqualTo("rhs")
	}

	@Test
	fun evalUtilArrowLhs() {
		utilArrow(expr(java("lhs")), expr(java("rhs")))
			.utilLhs
			.eval
			.assertEqualTo("lhs")
	}

	@Test
	fun evalUtilArrowRhs() {
		utilArrow(expr(java("lhs")), expr(java("rhs")))
			.utilRhs
			.eval
			.assertEqualTo("rhs")
	}
}
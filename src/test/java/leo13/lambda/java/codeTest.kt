package leo13.lambda.java

import leo.base.assertEqualTo
import leo13.lambda.*
import org.junit.Test
import kotlin.test.assertFails

class CodeTest {
	@Test
	fun code() {
		val a = expr(java("a"))
		val b = expr(java("b"))
		a.code.assertEqualTo("a")
		b.code.assertEqualTo("b")
		expr(arrow(a, b)).code.assertEqualTo("new Object[] {a, b}")
		expr(lhs(a)).code.assertEqualTo("(a)[0]")
		expr(rhs(a)).code.assertEqualTo("(a)[1]")
		expr(fn(a)).code.assertEqualTo("fn(v0 -> a)")
		expr(fn(expr(arg))).code.assertEqualTo("fn(v0 -> v0)")
		expr(fn(expr(fn(expr(arg))))).code.assertEqualTo("fn(v0 -> fn(v1 -> v1))")
		expr(fn(expr(fn(expr(arg.inc))))).code.assertEqualTo("fn(v0 -> fn(v1 -> v0))")
		expr(ap(a, b)).code.assertEqualTo("apply(a, b)")
		expr(arg).code(gen.inc.inc).assertEqualTo("v1")
		expr(arg.inc).code(gen.inc.inc).assertEqualTo("v0")
		assertFails { expr(arg.inc.inc).code(gen.inc.inc) }
	}

	@Test
	fun mainCode() {
		expr("Hello, world!").mainCode
	}
}
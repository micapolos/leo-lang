package leo13.lambda.java

import leo.base.assertEqualTo
import leo13.lambda.*
import org.junit.Test
import kotlin.test.assertFails

class CodeTest {
	@Test
	fun code() {
		val a = expr(java(code("a")))
		val b = expr(java(code("b")))
		a.code.assertEqualTo("a")
		b.code.assertEqualTo("b")
		expr(abstraction(a)).code.assertEqualTo("fn(v0 -> a)")
		expr(abstraction(expr(arg))).code.assertEqualTo("fn(v0 -> v0)")
		expr(abstraction(expr(abstraction(expr(arg))))).code.assertEqualTo("fn(v0 -> fn(v1 -> v1))")
		expr(abstraction(expr(abstraction(expr(arg.inc))))).code.assertEqualTo("fn(v0 -> fn(v1 -> v0))")
		expr(application(a, b)).code.assertEqualTo("apply(a, b)")
		expr(arg).code(gen.inc.inc).assertEqualTo("v1")
		expr(arg.inc).code(gen.inc.inc).assertEqualTo("v0")
		assertFails { expr(arg.inc.inc).code(gen.inc.inc) }
	}

	@Test
	fun mainCode() {
		expr(java("Hello, world!")).mainCode
	}
}
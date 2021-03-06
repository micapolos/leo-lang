package leo14.lambda.java

import leo.base.assertEqualTo
import leo14.lambda.*
import leo14.lambda.code.code
import leo14.lambda.code.gen
import leo14.lambda.code.inc
import org.junit.Test
import kotlin.test.assertFails

class CodeTest {
	@Test
	fun code() {
		val a = term(native(code("a")))
		val b = term(native(code("b")))
		a.code.assertEqualTo("a")
		b.code.assertEqualTo("b")
		fn(a).code.assertEqualTo("fn(v0 -> a)")
		fn(arg0).code.assertEqualTo("fn(v0 -> v0)")
		fn2(arg0).code.assertEqualTo("fn(v0 -> fn(v1 -> v1))")
		fn2(arg1).code.assertEqualTo("fn(v0 -> fn(v1 -> v0))")
		a(b).code.assertEqualTo("apply(a, b)")
		arg0.code(gen.inc.inc).assertEqualTo("v1")
		arg1.code(gen.inc.inc).assertEqualTo("v0")
		assertFails { arg2.code(gen.inc.inc) }
	}

	@Test
	fun mainCode() {
		term("Hello, world!").mainCode
	}
}
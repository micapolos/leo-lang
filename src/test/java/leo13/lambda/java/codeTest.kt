package leo13.lambda.java

import leo.base.assertEqualTo
import leo13.lambda.*
import org.junit.Test
import kotlin.test.assertFails

class CodeTest {
	@Test
	fun code() {
		val a = value(native(code("a")))
		val b = value(native(code("b")))
		a.code.assertEqualTo("a")
		b.code.assertEqualTo("b")
		value(abstraction(a)).code.assertEqualTo("fn(v0 -> a)")
		value(abstraction(value(arg))).code.assertEqualTo("fn(v0 -> v0)")
		value(abstraction(value(abstraction(value(arg))))).code.assertEqualTo("fn(v0 -> fn(v1 -> v1))")
		value(abstraction(value(abstraction(value(arg.inc))))).code.assertEqualTo("fn(v0 -> fn(v1 -> v0))")
		value(application(a, b)).code.assertEqualTo("apply(a, b)")
		value(arg).code(gen.inc.inc).assertEqualTo("v1")
		value(arg.inc).code(gen.inc.inc).assertEqualTo("v0")
		assertFails { value(arg.inc.inc).code(gen.inc.inc) }
	}

	@Test
	fun mainCode() {
		value("Hello, world!").mainCode
	}
}
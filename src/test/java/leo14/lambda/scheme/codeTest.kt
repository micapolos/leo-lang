package leo14.lambda.scheme

import leo.base.assertEqualTo
import leo14.lambda.code.gen
import leo14.lambda.code.inc
import leo14.lambda.fn
import leo14.lambda.fn2
import leo14.lambda.invoke
import kotlin.test.Test
import kotlin.test.assertFails

class GenTest {
	@Test
	fun schemeCode() {
		val a = term(code("a"))
		val b = term(code("b"))
		a.code.assertEqualTo(code("a"))
		b.code.assertEqualTo(code("b"))
		fn(a).code.assertEqualTo(code("(lambda (v0) a)"))
		fn(arg0).code.assertEqualTo(code("(lambda (v0) v0)"))
		fn2(arg0).code.assertEqualTo(code("(lambda (v0) (lambda (v1) v1))"))
		fn2(arg1).code.assertEqualTo(code("(lambda (v0) (lambda (v1) v0))"))
		a(b).code.assertEqualTo(code("(a b)"))
		arg0.code(gen.inc.inc).assertEqualTo(code("v1"))
		arg1.code(gen.inc.inc).assertEqualTo(code("v0"))
		assertFails { arg2.code(gen.inc.inc) }
	}
}
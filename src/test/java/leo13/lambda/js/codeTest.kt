package leo13.lambda.js

import leo.base.assertEqualTo
import leo13.lambda.code.code
import leo13.lambda.code.gen
import leo13.lambda.code.inc
import leo13.lambda.fn
import leo13.lambda.fn2
import leo13.lambda.invoke
import kotlin.test.Test
import kotlin.test.assertFails

class GenTest {
	@Test
	fun jsCode() {
		val a = value(code("a"))
		val b = value(code("b"))
		a.code.assertEqualTo("a")
		b.code.assertEqualTo("b")
		fn(a).code.assertEqualTo("v0=>a")
		fn(arg0).code.assertEqualTo("v0=>v0")
		fn2(arg0).code.assertEqualTo("v0=>v1=>v1")
		fn2(arg1).code.assertEqualTo("v0=>v1=>v0")
		a(b).code.assertEqualTo("(a)(b)")
		arg0.code(gen.inc.inc).assertEqualTo("v1")
		arg1.code(gen.inc.inc).assertEqualTo("v0")
		assertFails { arg2.code(gen.inc.inc) }
	}
}
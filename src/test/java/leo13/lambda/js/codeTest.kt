package leo13.lambda.js

import leo.base.assertEqualTo
import leo13.js2.id
import leo13.lambda.*
import kotlin.test.Test
import kotlin.test.assertFails
import leo13.js2.expr as jsExpr

class GenTest {
	@Test
	fun jsCode() {
		val a = value(jsExpr(id("a")))
		val b = value(jsExpr(id("b")))
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
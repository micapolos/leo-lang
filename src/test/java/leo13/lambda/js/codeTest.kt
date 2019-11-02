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
		val a = expr(jsExpr(id("a")))
		val b = expr(jsExpr(id("b")))
		a.jsCode.assertEqualTo("a")
		b.jsCode.assertEqualTo("b")
		expr(fn(a)).jsCode.assertEqualTo("v0=>a")
		expr(fn(expr(jsArg))).jsCode.assertEqualTo("v0=>v0")
		expr(fn(expr(fn(expr(jsArg))))).jsCode.assertEqualTo("v0=>v1=>v1")
		expr(fn(expr(fn(expr(jsArg.inc))))).jsCode.assertEqualTo("v0=>v1=>v0")
		expr(ap(a, b)).jsCode.assertEqualTo("(a)(b)")
		expr(jsArg).code(gen.inc.inc).assertEqualTo("v1")
		expr(jsArg.inc).code(gen.inc.inc).assertEqualTo("v0")
		assertFails { expr(jsArg.inc.inc).code(gen.inc.inc) }
	}
}
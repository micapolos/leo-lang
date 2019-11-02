package leo13.js2.lambda

import leo.base.assertEqualTo
import leo13.js2.id
import kotlin.test.Test
import kotlin.test.assertFails
import leo13.js2.expr as jsExpr

class JsTest {
	@Test
	fun jsCode() {
		val a = expr(jsExpr(id("a")))
		val b = expr(jsExpr(id("b")))
		a.jsCode.assertEqualTo("a")
		b.jsCode.assertEqualTo("b")
		expr(arrow(a, b)).jsCode.assertEqualTo("[a, b]")
		expr(lhs(a)).jsCode.assertEqualTo("(a)[0]")
		expr(rhs(a)).jsCode.assertEqualTo("(a)[1]")
		expr(fn(a)).jsCode.assertEqualTo("v0 => a")
		expr(fn(expr(jsArg))).jsCode.assertEqualTo("v0 => v0")
		expr(fn(expr(fn(expr(jsArg))))).jsCode.assertEqualTo("v0 => v1 => v1")
		expr(fn(expr(fn(expr(jsArg.inc))))).jsCode.assertEqualTo("v0 => v1 => v0")
		expr(ap(a, b)).jsCode.assertEqualTo("(a)(b)")
		expr(jsArg).code(jsGen.inc.inc).assertEqualTo("v1")
		expr(jsArg.inc).code(jsGen.inc.inc).assertEqualTo("v0")
		assertFails { expr(jsArg.inc.inc).code(jsGen.inc.inc) }
	}
}
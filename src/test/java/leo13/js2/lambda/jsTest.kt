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
		expr(lhs(a)).jsCode.assertEqualTo("a[0]")
		expr(rhs(a)).jsCode.assertEqualTo("a[1]")
		expr(fn(a)).jsCode.assertEqualTo("function(v0) { return a; }")
		expr(fn(expr<Js>(arg(0)))).jsCode.assertEqualTo("function(v0) { return v0; }")
		expr(fn(expr(fn(expr<Js>(arg(0)))))).jsCode.assertEqualTo("function(v0) { return function(v1) { return v1; }; }")
		expr(fn(expr(fn(expr<Js>(arg(1)))))).jsCode.assertEqualTo("function(v0) { return function(v1) { return v0; }; }")
		expr(ap(a, b)).jsCode.assertEqualTo("a(b)")
		expr<Js>(arg(0)).code(JsGen(10)).assertEqualTo("v9")
		expr<Js>(arg(2)).code(JsGen(10)).assertEqualTo("v7")
		expr<Js>(arg(9)).code(JsGen(10)).assertEqualTo("v0")
		assertFails { expr<Js>(arg(10)).code(JsGen(10)) }
	}
}
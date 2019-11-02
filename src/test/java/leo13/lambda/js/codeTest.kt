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
		a.jsCode.assertEqualTo("a")
		b.jsCode.assertEqualTo("b")
		value(abstraction(a)).jsCode.assertEqualTo("v0=>a")
		value(abstraction(value(jsArg))).jsCode.assertEqualTo("v0=>v0")
		value(abstraction(value(abstraction(value(jsArg))))).jsCode.assertEqualTo("v0=>v1=>v1")
		value(abstraction(value(abstraction(value(jsArg.inc))))).jsCode.assertEqualTo("v0=>v1=>v0")
		value(application(a, b)).jsCode.assertEqualTo("(a)(b)")
		value(jsArg).code(gen.inc.inc).assertEqualTo("v1")
		value(jsArg.inc).code(gen.inc.inc).assertEqualTo("v0")
		assertFails { value(jsArg.inc.inc).code(gen.inc.inc) }
	}
}
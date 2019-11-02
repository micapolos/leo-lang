package leo13.js2

import leo.base.assertEqualTo
import org.junit.Test

class CodeTest {
	@Test
	fun fnCode() {
		fn(args(), block())
			.exprCode
			.assertEqualTo("function(){}")

		fn(args(), block(stmt(ret(expr("jajko")))))
			.exprCode
			.assertEqualTo("function(){return 'jajko';}")

		fn(args(), block(stmt(expr("jajko")), stmt(expr("kura"))))
			.exprCode
			.assertEqualTo("function(){'jajko';'kura';}")

		fn(args("x1"), block())
			.exprCode
			.assertEqualTo("function(x1){}")

		fn(args("x1", "x2"), block())
			.exprCode
			.assertEqualTo("function(x1,x2){}")
	}

	@Test
	fun getCode() {
		expr("jajko").get("kura").code.assertEqualTo("('jajko').kura")
	}

	@Test
	fun atCode() {
		expr("jajko")[expr("kura")].code.assertEqualTo("('jajko')['kura']")
	}

	@Test
	fun apCode() {
		expr(id("foo"))().code.assertEqualTo("(foo)()")
		expr(id("sin"))(expr(1)).code.assertEqualTo("(sin)(1)")
		expr(id("max"))(expr(1), expr(2)).code.assertEqualTo("(max)(1,2)")
		expr(id("foo")).get("round")().code.assertEqualTo("((foo).round)()")
	}
}
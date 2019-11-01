package leo13.js2

import leo.base.assertEqualTo
import org.junit.Test

class CodeTest {
	@Test
	fun fnCode() {
		fn(args(), seq())
			.code
			.assertEqualTo("function() {  }")

		fn(args(), seq(expr("jajko")))
			.code
			.assertEqualTo("function() { return 'jajko'; }")

		fn(args(), seq(expr("jajko"), expr("kura")))
			.code
			.assertEqualTo("function() { 'jajko'; return 'kura'; }")

		fn(args("x1"), seq())
			.code
			.assertEqualTo("function(x1) {  }")

		fn(args("x1", "x2"), seq())
			.code
			.assertEqualTo("function(x1, x2) {  }")
	}

	@Test
	fun getCode() {
		expr("jajko").get("kura").code.assertEqualTo("'jajko'.kura")
	}

	@Test
	fun atCode() {
		expr("jajko")[expr("kura")].code.assertEqualTo("'jajko'['kura']")
	}

	@Test
	fun apCode() {
		expr(id("foo"))().code.assertEqualTo("foo()")
		expr(id("sin"))(expr(1)).code.assertEqualTo("sin(1)")
		expr(id("max"))(expr(1), expr(2)).code.assertEqualTo("max(1, 2)")
		expr(id("foo")).get("round")().code.assertEqualTo("foo.round()")
	}
}
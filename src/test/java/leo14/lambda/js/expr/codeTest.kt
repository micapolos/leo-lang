package leo14.lambda.js.expr

import leo.base.assertEqualTo
import leo14.code.code
import leo14.lambda.arg0
import leo14.lambda.arg1
import leo14.lambda.fn
import leo14.lambda.invoke
import kotlin.test.Test

class CodeTest {
	@Test
	fun code() {
		"document.body".code.expr.term
			.code.string.assertEqualTo("document.body")
	}

	@Test
	fun invoke() {
		"window.alert".code.expr.term
			.invoke("123".code.expr.term)
			.code.string.assertEqualTo("(window.alert)(123)")
	}

	@Test
	fun fn() {
		fn(fn(arg1<Expr>().op("+", arg0()).expr.term)).code.string.assertEqualTo("v0=>v1=>(v0)+(v1)")
	}

	@Test
	fun get() {
		"window".code.expr.term
			.get("alert").expr.term
			.code.string.assertEqualTo("(window).alert")
	}

	@Test
	fun op() {
		"2".code.expr.term
			.op("+", "3".code.expr.term)
			.expr.term
			.code.string.assertEqualTo("(2)+(3)")
	}
}
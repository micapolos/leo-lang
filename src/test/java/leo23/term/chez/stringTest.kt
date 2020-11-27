package leo23.term.chez

import leo.base.assertEqualTo
import leo23.term.apply
import leo23.term.argExpr
import leo23.term.does
import leo23.term.doesRecursively
import leo23.term.expr
import leo23.term.numberMinus
import leo23.term.params
import leo23.term.textAppend
import leo23.term.tuple
import leo23.term.tupleAt
import leo23.term.type.does
import leo23.term.type.numberType
import kotlin.test.Test

class StringTest {
	@Test
	fun stringAppend() {
		expr("Hello, ")
			.textAppend(expr("world!"))
			.string
			.assertEqualTo("(string-append \"Hello, \" \"world!\")")
	}

	@Test
	fun fn() {
		params(numberType, numberType)
			.does(argExpr(1, numberType).numberMinus(argExpr(0, numberType)))
			.string
			.assertEqualTo("(lambda (v0 v1) (- v0 v1))")
	}

	@Test
	fun recfn() {
		params(numberType)
			.doesRecursively(argExpr(1, params(numberType).does(numberType)).apply(argExpr(0, numberType)))
			.string
			.assertEqualTo("(letrec ((v0 (lambda (v1) (v0 v1)))) v0)")
	}

	@Test
	fun vectorAt() {
		tuple(expr(10), expr(20))
			.tupleAt(0)
			.string
			.assertEqualTo("(vector-ref (vector 10 20) 0)")
	}
}
package leo14.lambda.syntax

import leo.base.assertEqualTo
import leo13.stack
import leo14.lambda.arg
import leo14.lambda.fn
import leo14.lambda.invoke
import leo21.term.arg
import kotlin.test.Test

class SyntaxTest {
	@Test
	fun variable() {
		VariableSyntax<Int>().let { variable ->
			variable.term(stack(variable))
		}.assertEqualTo(arg(0))
	}

	@Test
	fun fn1() {
		fn<Int> { it }.term.assertEqualTo(fn(arg(0)))
	}

	@Test
	fun fn2() {
		fn<Int> { a -> fn { b -> a.invoke(b) } }.term.assertEqualTo(fn(fn(arg(1).invoke(arg(0)))))
	}

	@Test
	fun fix_() {
		fix<Int>().term.assertEqualTo(leo14.lambda.fix())
	}

	@Test
	fun recFn_() {
		recFn<Int> { f, v -> f(v) }
			.term
			.assertEqualTo(leo14.lambda.recFn(arg<Int>(1).invoke(arg<Int>(0))))
	}
}
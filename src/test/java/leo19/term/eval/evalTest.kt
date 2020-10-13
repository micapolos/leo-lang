package leo19.term.eval

import leo.base.assertEqualTo
import leo19.expr.eval.scope
import leo19.term.expr.expr
import leo19.term.function
import leo19.term.get
import leo19.term.invoke
import leo19.term.nullTerm
import leo19.term.term
import leo19.term.variable
import leo19.value.nullValue
import leo19.value.value
import kotlin.test.Test

class EvalTest {
	@Test
	fun nul() {
		nullTerm
			.eval
			.assertEqualTo(nullValue)
	}

	@Test
	fun int() {
		term(12)
			.eval
			.assertEqualTo(value(12))
	}

	@Test
	fun array() {
		term(term(10), term(20))
			.eval
			.assertEqualTo(value(value(10), value(20)))
	}

	@Test
	fun arrayGet() {
		val tuple = term(term(10), term(20))
		tuple.get(term(0)).eval.assertEqualTo(value(10))
		tuple.get(term(1)).eval.assertEqualTo(value(20))
	}

	@Test
	fun function() {
		term(function(term(10)))
			.eval
			.assertEqualTo(value(leo19.value.function(scope(), expr(10))))
	}

	@Test
	fun invoke() {
		term(function(term(variable(0))))
			.invoke(term(10))
			.eval
			.assertEqualTo(value(10))
	}

	@Test
	fun variable() {
		val scope = scope(value(10), value(20))
		scope.eval(term(variable(0))).assertEqualTo(value(20))
		scope.eval(term(variable(1))).assertEqualTo(value(10))
	}
}
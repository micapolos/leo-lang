package leo19.term.eval

import leo.base.assertEqualTo
import leo19.term.functionTerm
import leo19.term.get
import leo19.term.invoke
import leo19.term.term
import leo19.term.variableTerm
import kotlin.test.Test

class EvalTest {
	@Test
	fun int() {
		term(12)
			.eval
			.assertEqualTo(value(12))
	}

	@Test
	fun tuple() {
		term(term(10), term(20))
			.eval
			.assertEqualTo(value(value(10), value(20)))
	}

	@Test
	fun tupleGet() {
		val tuple = term(term(10), term(20))
		tuple.get(term(0)).eval.assertEqualTo(value(10))
		tuple.get(term(1)).eval.assertEqualTo(value(20))
	}

	@Test
	fun function() {
		functionTerm(term(10))
			.eval
			.assertEqualTo(functionValue(scope(), term(10)))
	}

	@Test
	fun invoke() {
		functionTerm(variableTerm(0))
			.invoke(term(10))
			.eval
			.assertEqualTo(value(10))
	}

	@Test
	fun variable() {
		val scope = scope(value(10), value(20))
		scope.eval(variableTerm(0)).assertEqualTo(value(20))
		scope.eval(variableTerm(1)).assertEqualTo(value(10))
	}
}
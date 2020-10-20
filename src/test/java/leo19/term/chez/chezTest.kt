package leo19.term.chez

import leo.base.assertEqualTo
import leo19.term.function
import leo19.term.get
import leo19.term.invoke
import leo19.term.lhs
import leo19.term.nullTerm
import leo19.term.recursiveFunction
import leo19.term.rhs
import leo19.term.term
import leo19.term.to
import leo19.term.variable
import kotlin.test.Test

class ChezTest {
	@Test
	fun nul() {
		nullTerm
			.chez
			.assertEqualTo("'()")
	}

	@Test
	fun int() {
		term(10)
			.chez
			.assertEqualTo("10")
	}

	@Test
	fun array() {
		term(term(10), term(20))
			.chez
			.assertEqualTo("(vector 10 20)")
	}

	@Test
	fun pair() {
		term(10).to(term(20))
			.chez
			.assertEqualTo("(cons 10 20)")
	}

	@Test
	fun lhs() {
		term(10).lhs
			.chez
			.assertEqualTo("(car 10)")
	}

	@Test
	fun rhs() {
		term(10).rhs
			.chez
			.assertEqualTo("(cdr 10)")
	}

	@Test
	fun arrayGet() {
		term(term(10), term(20))
			.get(term(0))
			.chez
			.assertEqualTo("(vector-ref (vector 10 20) 0)")
	}

	@Test
	fun function() {
		term(function(term(variable(0))))
			.chez
			.assertEqualTo("(lambda (v0) v0)")
	}

	@Test
	fun recursiveFunction() {
		term(recursiveFunction(term(variable(0))))
			.chez
			.assertEqualTo("(letrec ((v0 (lambda (v1) v1))) v0)")
	}

	@Test
	fun deepFunction() {
		term(function(term(function(term(variable(1)).invoke(term(variable(0)))))))
			.chez
			.assertEqualTo("(lambda (v0) (lambda (v1) (v0 v1)))")
	}

	@Test
	fun invoke() {
		term(function(term(variable(0))))
			.invoke(term(10))
			.chez
			.assertEqualTo("((lambda (v0) v0) 10)")
	}
}
package leo19.term.chez

import leo.base.assertEqualTo
import leo19.term.function
import leo19.term.get
import leo19.term.invoke
import leo19.term.term
import leo19.term.variable
import kotlin.test.Test

class ChezTest {
	@Test
	fun int() {
		term(10)
			.chez
			.assertEqualTo("10")
	}

	@Test
	fun tuple() {
		term(term(10), term(20))
			.chez
			.assertEqualTo("(vector 10 20)")
	}

	@Test
	fun tupleGet() {
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
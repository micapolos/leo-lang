package leo14.lambda.eval

import leo.base.assertEqualTo
import leo13.index0
import leo13.index1
import leo13.index2
import leo13.stack
import leo14.dsl.it
import leo14.dsl.native
import leo14.lambda.*
import org.junit.Test

class EvalTest {
	@Test
	fun helloWorld() {
		term("Hello, world!")
			.eval
			.assertEqualTo("Hello, world!")
	}

	@Test
	fun invoke() {
		fn(arg0)(term("Hello, world!"))
			.eval
			.assertEqualTo("Hello, world!")
	}

	@Test
	fun pairFirst() {
		pair(term("first"), term("second"))
			.first
			.eval
			.assertEqualTo("first")
	}

	@Test
	fun pairSecond() {
		pair(term("first"), term("second"))
			.second
			.eval
			.assertEqualTo("second")
	}

	@Test
	fun evalScript() {
		native(it("Hello, world!")).eval.assertEqualTo("Hello, world!")
	}

	@Test
	fun choiceMatch() {
		choiceTerm(index0, index2, term("foo"))
			.matchTerm(fn(fn(term("case1"))), fn(fn(term("case0"))))
			.eval
			.assertEqualTo(function(stack("foo"), term("case0")))

		choiceTerm(index1, index2, term("foo"))
			.matchTerm(fn(fn(term("case1"))), fn(fn(term("case0"))))
			.eval
			.assertEqualTo(function(stack("foo"), term("case1")))
	}
}
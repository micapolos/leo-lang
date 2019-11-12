package leo14.typed.compiler

import leo.base.assertEqualTo
import leo13.stack
import leo14.lambda.arg0
import leo14.lambda.fn
import leo14.lambda.invoke
import leo14.lambda.term
import leo14.typed.choice
import leo14.typed.of
import leo14.typed.optionTo
import leo14.typed.type
import kotlin.test.Test
import kotlin.test.assertFails

class MatchTest {
	@Test
	fun matchBegin() {
		Match(
			term("lhs"),
			stack(
				"one" optionTo type("zar"),
				"zero" optionTo type("zoo")),
			null)
			.begin("zero")
			.assertEqualTo(
				Case(
					Match(
						term("lhs"),
						stack("one" optionTo type("zar")),
						null),
					arg0<String>() of type("zoo")))
	}

	@Test
	fun matchBeginMismatch() {
		assertFails {
			Match(
				term("lhs"),
				stack("zero" optionTo type("zoo")),
				null)
				.begin("one")
		}
	}

	@Test
	fun matchBeginExhausted() {
		assertFails {
			Match(
				term("lhs"),
				stack(),
				null)
				.begin("one")
		}
	}

	@Test
	fun matchLineEnd() {
		Case(
			Match(
				term("lhs"),
				stack("one" optionTo type("zar")),
				type(choice("zoo", "zar"))),
			term("case") of type(choice("zoo", "zar")))
			.end()
			.assertEqualTo(
				Match(
					term("lhs").invoke(fn(term("case"))),
					stack("one" optionTo type("zar")),
					type(choice("zoo", "zar"))))
	}

	@Test
	fun matchLineEndTypeMismatch() {
		assertFails {
			Case(
				Match(
					term("lhs"),
					stack(),
					type("foo")),
				term("case1") of type("bar"))
				.end()
		}
	}

	@Test
	fun matchEnd() {
		Match(
			term("lhs"),
			stack(),
			type("foo"))
			.end()
			.assertEqualTo(term("lhs") of type("foo"))
	}

	@Test
	fun matchEndNotExhausted() {
		assertFails {
			Match(
				term("lhs"),
				stack("zero" optionTo type("zoo")),
				type("foo"))
				.end()
		}
	}

	@Test
	fun matchEndImpossible() {
		assertFails {
			Match(
				term("lhs"),
				stack(),
				null)
				.end()
		}
	}
}
package lambda.v2

import leo.base.assertEqualTo
import leo.base.inc
import leo.base.nat
import leo.binary.zero
import kotlin.test.Test

class QuoteTest {
	@Test
	fun nat() {
		zero.nat.term.natOrNull.assertEqualTo(zero.nat)
		zero.nat.inc.term.natOrNull.assertEqualTo(zero.nat.inc)
		zero.nat.inc.inc.term.natOrNull.assertEqualTo(zero.nat.inc.inc)
	}

	@Test
	fun term() {
		argument(0.nat).quotedTerm.unquoteArgument.assertEqualTo(argument(0.nat))
		argument(1.nat).quotedTerm.unquoteArgument.assertEqualTo(argument(1.nat))
		application(arg0(0), arg0(1)).quotedTerm.unquoteApplication.assertEqualTo(application(arg0(0), arg0(1)))
		function(body(arg0(0))).quotedTerm.unquoteFunction.assertEqualTo(function(body(arg0(0))))
		quote.quotedTerm.unquoteQuote.assertEqualTo(quote)
		unquote.quotedTerm.unquoteUnquote.assertEqualTo(unquote)

		arg0(0).quotedTerm.unquoteTerm.assertEqualTo(arg0(0))
		arg0(1).quotedTerm.unquoteTerm.assertEqualTo(arg0(1))
		arg0(0).apply(arg0(1)).quotedTerm.unquoteTerm.assertEqualTo(arg0(0).apply(arg0(1)))
		fn { arg.term }.quotedTerm.unquoteTerm.assertEqualTo(fn { arg.term })
		term(quote).quotedTerm.unquoteTerm.assertEqualTo(term(quote))
		term(unquote).quotedTerm.unquoteTerm.assertEqualTo(term(unquote))
	}

	@Test
	fun evalQuoting() {
		term(unquote)(term(quote)(arg0(0))).eval.assertEqualTo(arg0(0))
	}
}

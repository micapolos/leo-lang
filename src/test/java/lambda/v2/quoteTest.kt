package lambda.v2

import leo.base.assertEqualTo
import leo.base.inc
import leo.base.nat
import leo.binary.zero
import kotlin.test.Test

class QuoteTest {
	@Test
	fun nat() {
		zero.nat.quotedTerm.natOrNull.assertEqualTo(zero.nat)
		zero.nat.inc.quotedTerm.natOrNull.assertEqualTo(zero.nat.inc)
		zero.nat.inc.inc.quotedTerm.natOrNull.assertEqualTo(zero.nat.inc.inc)
	}

	@Test
	fun term() {
		argument(0.nat).quotedTerm.unquoteArgument.assertEqualTo(argument(0.nat))
		argument(1.nat).quotedTerm.unquoteArgument.assertEqualTo(argument(1.nat))
		application(arg(0), arg(1)).quotedTerm.unquoteApplication.assertEqualTo(application(arg(0), arg(1)))
		function(arg(0)).quotedTerm.unquoteFunction.assertEqualTo(function(arg(0)))
		quote.quotedTerm.unquoteQuote.assertEqualTo(quote)
		unquote.quotedTerm.unquoteUnquote.assertEqualTo(unquote)

		arg(0).quotedTerm.unquoteTerm.assertEqualTo(arg(0))
		arg(1).quotedTerm.unquoteTerm.assertEqualTo(arg(1))
		arg(0).apply(arg(1)).quotedTerm.unquoteTerm.assertEqualTo(arg(0).apply(arg(1)))
		fn { arg(0) }.quotedTerm.unquoteTerm.assertEqualTo(fn { arg(0) })
		term(quote).quotedTerm.unquoteTerm.assertEqualTo(term(quote))
		term(unquote).quotedTerm.unquoteTerm.assertEqualTo(term(unquote))
	}

	@Test
	fun evalQuoting() {
		term(unquote)(term(quote)(arg(0))).assertEqualTo(arg(0))
	}
}

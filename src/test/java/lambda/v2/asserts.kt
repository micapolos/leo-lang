package lambda.v2

import leo.base.assertEqualTo

fun Term.assertEvalsTo(term: Term) =
	assertEqualTo(term.eval)
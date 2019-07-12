package lambda.indexed

import leo.base.assertEqualTo

fun Term.assertEvalsTo(term: Term) =
	assertEqualTo(term.eval)
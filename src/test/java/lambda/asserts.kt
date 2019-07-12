package lambda

import lambda.indexed.eval
import lambda.indexed.script
import leo.base.assertEqualTo
import leo.base.string

fun Term.assertEvalsTo(term: Term) =
	indexed.eval.script.string.assertEqualTo(term.indexed.eval.script.string)
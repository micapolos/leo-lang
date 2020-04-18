package leo15.terms

import leo.base.assertEqualTo
import leo15.lambda.Term
import leo15.lambda.eval

fun Term.assertGives(term: Term) =
	eval.assertEqualTo(term)
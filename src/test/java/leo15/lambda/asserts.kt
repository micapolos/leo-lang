package leo15.lambda

import leo.base.assertEqualTo

val Term.assertEvaluatesOnce: Term
	get() {
		var evaluated = false
		return termFn { term ->
			evaluated.assertEqualTo(false, "Evaluated twice")
			evaluated = true
			term
		}.invoke(this)
	}

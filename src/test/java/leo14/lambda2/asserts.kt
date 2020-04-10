package leo14.lambda2

import leo.base.assertEqualTo

val Term.assertEvaluatesOnce: Term
	get() {
		var evaluated = false
		return fn { term ->
			evaluated.assertEqualTo(false, "Evaluated twice")
			evaluated = true
			term
		}(this)
	}

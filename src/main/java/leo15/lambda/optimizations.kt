package leo15.lambda

import leo.base.ifOrNull

// TODO: It's better to optimize constant propagation rather that optimizing post-fact.

val Term.optimize: Term
	get() =
		null
			?: optimizePairUnpair
			?: this

val Term.optimizePairUnpair: Term?
	get() =
		applicationOrNull?.let { application ->
			application.rhs.unpairOrNull?.let { (a, b) ->
				application.lhs.abstractionOrNull?.let { abstraction ->
					abstraction.body.applicationOrNull?.let { outerApplication ->
						outerApplication.lhs.applicationOrNull?.let { innerApplication ->
							ifOrNull(outerApplication.rhs == pair(at(0)(first))(at(0)(second))(second)) {
								ifOrNull(innerApplication.rhs == pair(at(0)(first))(at(0)(second))(first)) {
									innerApplication.lhs.invoke(a).invoke(b)
								}
							}
						}
					}
				}
			}
		}

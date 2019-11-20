package leo14.lambda

import leo13.Index
import leo13.max
import leo13.previousOrNull

val Term<*>.freeIndexOrNull: Index?
	get() =
	when (this) {
		is NativeTerm -> null
		is AbstractionTerm -> abstraction.body.freeIndexOrNull?.previousOrNull
		is ApplicationTerm -> {
			val lhsIndexOrNull = application.lhs.freeIndexOrNull
			val rhsIndexOrNull = application.rhs.freeIndexOrNull
			when {
				lhsIndexOrNull == null -> rhsIndexOrNull
				rhsIndexOrNull == null -> lhsIndexOrNull
				else -> lhsIndexOrNull.max(rhsIndexOrNull)
			}
		}
		is VariableTerm -> variable.index
	}

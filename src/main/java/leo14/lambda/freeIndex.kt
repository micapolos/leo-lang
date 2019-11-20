package leo14.lambda

import leo13.Index
import leo13.max
import leo13.next

fun Term<*>.freeIndexOrNull(depth: Index): Index? =
	when (this) {
		is NativeTerm -> null
		is AbstractionTerm -> abstraction.body.freeIndexOrNull(depth.next)
		is ApplicationTerm -> {
			val lhsIndexOrNull = application.lhs.freeIndexOrNull(depth)
			val rhsIndexOrNull = application.rhs.freeIndexOrNull(depth)
			when {
				lhsIndexOrNull == null -> rhsIndexOrNull
				rhsIndexOrNull == null -> lhsIndexOrNull
				else -> lhsIndexOrNull.max(rhsIndexOrNull)
			}
		}
		is VariableTerm -> variable.index
	}

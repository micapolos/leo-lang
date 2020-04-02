package leo14.lambda

import leo13.Index
import kotlin.math.max

val Term<*>.freeVariableCount: Index
	get() =
		when (this) {
			is NativeTerm -> 0
			is AbstractionTerm -> max(abstraction.body.freeVariableCount.dec(), 0)
			is ApplicationTerm -> max(application.lhs.freeVariableCount, application.rhs.freeVariableCount)
			is VariableTerm -> variable.index.inc()
		}

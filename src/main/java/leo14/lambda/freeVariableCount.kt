package leo14.lambda

import leo13.*

val Term<*>.freeVariableCount: Index
	get() =
	when (this) {
		is NativeTerm -> index0
		is AbstractionTerm -> abstraction.body.freeVariableCount.previousOrZero
		is ApplicationTerm -> application.lhs.freeVariableCount.max(application.rhs.freeVariableCount)
		is VariableTerm -> variable.index.next
	}

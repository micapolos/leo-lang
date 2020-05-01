package leo16

import leo15.givesName
import leo15.isName

fun Scope.applyDefinition(value: Value): Scope? =
	null
		?: applyIs(value)
		?: applyGives(value)

fun Scope.applyIs(value: Value): Scope? =
	value.matchInfix(isName) { lhs, rhs ->
		plus(lhs.script.exactPattern.bindingTo(rhs.body))
	}

fun Scope.applyGives(value: Value): Scope? =
	value.matchInfix(givesName) { lhs, rhs ->
		plus(lhs.script.pattern.bindingTo(function(rhs.script).body))
	}

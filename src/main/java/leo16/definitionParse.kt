package leo16

import leo15.givesName
import leo15.isName

fun Dictionary.definitionOrNull(value: Value): Definition? =
	null
		?: isDefinitionOrNull(value)
		?: givesDefinitionOrNull(value)

fun Dictionary.isDefinitionOrNull(value: Value): Definition? =
	value.matchInfix(isName) { lhs, rhs ->
		lhs.pattern.definitionTo(emptyScope.emptyEvaluator.plus(rhs).evaluated.value.body)
	}

fun Dictionary.givesDefinitionOrNull(value: Value): Definition? =
	value.matchInfix(givesName) { lhs, rhs ->
		lhs.pattern.definitionTo(function(rhs).body)
	}

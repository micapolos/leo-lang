package leo16

import leo16.names.*

fun Dictionary.definitionOrNull(value: Value): Definition? =
	null
		?: isDefinitionOrNull(value)
		?: givesDefinitionOrNull(value)
		?: expandsDefinitionOrNull(value)

fun Dictionary.isDefinitionOrNull(value: Value): Definition? =
	value.matchInfix(_is) { lhs, rhs ->
		lhs.pattern.definitionTo(emptyScope.emptyEvaluator.plus(rhs).evaluated.value.body)
	}

fun Dictionary.givesDefinitionOrNull(value: Value): Definition? =
	value.matchInfix(_gives) { lhs, rhs ->
		lhs.pattern.definitionTo(function(rhs).body)
	}

fun Dictionary.expandsDefinitionOrNull(value: Value): Definition? =
	value.matchInfix(_expands) { lhs, rhs ->
		lhs.pattern.macroTo(function(rhs).body)
	}

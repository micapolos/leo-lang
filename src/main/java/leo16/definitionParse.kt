package leo16

import leo15.givesName
import leo15.isName

fun Dictionary.definitionOrNull(value: Value): Definition? =
	null
		?: value.isDefinitionOrNull
		?: givesDefinitionOrNull(value)

val Value.isDefinitionOrNull: Definition?
	get() =
		matchInfix(isName) { lhs, rhs ->
			lhs.pattern.definitionTo(rhs.body)
		}

fun Dictionary.givesDefinitionOrNull(value: Value): Definition? =
	value.matchInfix(givesName) { lhs, rhs ->
		lhs.pattern.definitionTo(function(rhs).body)
	}

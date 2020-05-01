package leo16

import leo15.givesName
import leo15.isName

fun Library.bindingOrNull(value: Value): Binding? =
	null
		?: value.isBindingOrNull
		?: givesBindingOrNull(value)

val Value.isBindingOrNull: Binding?
	get() =
		matchInfix(isName) { lhs, rhs ->
			lhs.script.exactPattern.bindingTo(rhs.body)
		}

fun Library.givesBindingOrNull(value: Value): Binding? =
	value.matchInfix(givesName) { lhs, rhs ->
		lhs.script.pattern.bindingTo(function(rhs.script).body)
	}

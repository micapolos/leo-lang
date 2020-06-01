package leo16

import leo.base.notNullIf
import leo15.lambda.invoke

data class TypedDefinition(val type: Type, val typed: Typed, val isConstant: Boolean)

fun Type.constantDefinitionTo(typed: Typed) = TypedDefinition(this, typed, isConstant = true)
fun Type.functionDefinitionTo(typed: Typed) = TypedDefinition(this, typed, isConstant = false)

fun TypedDefinition.apply(argTyped: Typed): Typed? =
	notNullIf(type == argTyped.type) {
		if (isConstant) typed
		else typed.term.invoke(argTyped.term) of typed.type
	}

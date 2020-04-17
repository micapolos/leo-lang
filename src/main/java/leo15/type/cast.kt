package leo15.type

import leo.base.Parameter
import leo.base.notNullIf
import leo.base.parameter

val castRecursiveParameter: Parameter<Recursive?> = parameter(null)

fun Typed.castExpression(to: Type): Expression? =
	when (to) {
		EmptyType -> notNullIf(type.isEmpty) { expression }
		is LinkType -> TODO()
		is RepeatingType -> TODO()
		is RecursiveType -> TODO()
		RecurseType -> TODO()
	}

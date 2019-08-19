package leo13

import leo.base.notNullIf
import leo9.Stack
import leo9.mapFirst
import leo9.push
import leo9.stack

data class Types(val typeStack: Stack<Type>)

val Stack<Type>.types get() = Types(this)
fun types() = stack<Type>().types
fun Types.plus(type: Type) = typeStack.push(type).types
fun Types.containingType(type: Type): Type =
	typeStack.mapFirst {
		notNullIf(contains(type)) {
			this
		}
	} ?: type

fun Types.resolveType(typedValue: TypedValue): TypedValue =
	typedValue.value of containingType(typedValue.type)

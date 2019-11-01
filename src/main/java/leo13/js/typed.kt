package leo13.js

import leo.base.ifNotNull
import leo.base.orIfNull

data class Typed(val expression: Expression, val types: Types)

infix fun Expression.of(types: Types) = Typed(this, types)
val nullTyped = nullExpression of emptyTypes

fun typed(number: Number) = expression(number) of types(numberType)
fun typed(string: String) = expression(string) of types(stringType)

fun Typed.plus(string: String, rhs: Typed) =
	expression.then(rhs.expression) of types.plus(type(string fieldTo rhs.types))

fun Typed.resolve(functions: Functions): Typed =
	functions
		.indexedAt(types)
		.ifNotNull { expression(invoke(it.index, expression)) of it.value.bodyTyped.types }
		.orIfNull { this }

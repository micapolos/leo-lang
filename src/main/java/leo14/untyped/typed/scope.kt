package leo14.untyped.typed

import leo.base.the
import leo14.lambda.runtime.Value

sealed class Scope
object EmptyScope : Scope()
data class LinkScope(val parentOrNull: Scope?, val definition: Definition) : Scope()

val emptyScope: Scope = EmptyScope
fun Scope.plus(definition: Definition): Scope = LinkScope(this, definition)

tailrec fun Scope.apply(typed: Typed): Typed? =
	when (this) {
		is EmptyScope -> null
		is LinkScope -> definition.apply(typed) ?: parentOrNull?.apply(typed)
	}

fun Scope.applyValue(value: Value): Value? =
	apply(value.valueSelfTyped)?.value?.the

fun Scope.bindValue(value: Value): Scope =
	TODO()

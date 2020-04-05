package leo14.untyped.typed

import leo.base.Empty
import leo.base.the
import leo14.lambda.runtime.Value

sealed class Scope
data class EmptyScope(val empty: Empty) : Scope()
data class LinkScope(val parentOrNull: Scope?, val definition: Definition) : Scope()

val Empty.scope: Scope get() = EmptyScope(this)
fun Scope.plus(definition: Definition): Scope = LinkScope(this, definition)

tailrec fun Scope.apply(typed: Typed): Typed? =
	when (this) {
		is EmptyScope -> null
		is LinkScope -> definition.apply(typed) ?: parentOrNull?.apply(typed)
	}

fun Scope.applyValue(value: Value): Value? =
	apply(value.valueSelfTyped)?.value?.the

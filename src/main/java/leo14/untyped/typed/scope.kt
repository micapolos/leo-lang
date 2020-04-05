package leo14.untyped.typed

import leo.base.the
import leo14.lambda.runtime.Value

data class Scope(val parentOrNull: Scope?, val definition: Definition)

fun scope(definition: Definition) = Scope(null, definition)
fun Scope.plus(definition: Definition) = Scope(this, definition)

tailrec fun Scope.apply(typed: Typed): Typed? =
	definition.apply(typed) ?: parentOrNull?.apply(typed)

fun Scope.applyValue(value: Value): Value? =
	apply(value.valueSelfTyped)?.value?.the

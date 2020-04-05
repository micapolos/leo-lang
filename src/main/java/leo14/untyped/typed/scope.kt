package leo14.untyped.typed

import leo14.lambda.runtime.Value

data class Scope(val parentOrNull: Scope?, val definition: Definition)

fun Scope.atType(type: Value): Value = definition.atType(type) ?: parentOrNull?.atType(type)
fun Scope.matchingDefinition(value: Value): Definition = TODO()
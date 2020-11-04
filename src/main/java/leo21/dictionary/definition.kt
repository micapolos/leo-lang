package leo21.dictionary

import leo21.compiled.Binding
import leo21.compiled.binding
import leo21.typed.ArrowTyped
import leo21.typed.Typed

sealed class Definition
data class FunctionDefinition(val arrowTyped: ArrowTyped) : Definition()
data class ConstantDefinition(val typed: Typed) : Definition()

fun definition(arrowTyped: ArrowTyped): Definition = FunctionDefinition(arrowTyped)

val Definition.binding: Binding
	get() =
		when (this) {
			is FunctionDefinition -> binding(arrowTyped.arrow)
			is ConstantDefinition -> TODO()
		}
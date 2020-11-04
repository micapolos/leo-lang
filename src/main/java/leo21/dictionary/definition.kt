package leo21.dictionary

import leo14.Script
import leo14.lambda.fn
import leo14.matchInfix
import leo21.compiled.Binding
import leo21.compiled.Bindings
import leo21.compiled.constantBinding
import leo21.compiled.functionBinding
import leo21.compiled.push
import leo21.compiled.typed
import leo21.type.Type
import leo21.type.arrowTo
import leo21.type.type
import leo21.typed.ArrowTyped
import leo21.typed.Typed
import leo21.typed.of

sealed class Definition
data class FunctionDefinition(val arrowTyped: ArrowTyped) : Definition()
data class ConstantDefinition(val type: Type, val typed: Typed) : Definition()

fun definition(arrowTyped: ArrowTyped): Definition = FunctionDefinition(arrowTyped)
fun definition(type: Type, typed: Typed): Definition = ConstantDefinition(type, typed)

val Definition.binding: Binding
	get() =
		when (this) {
			is FunctionDefinition -> functionBinding(arrowTyped.arrow)
			is ConstantDefinition -> constantBinding(type arrowTo typed.type)
		}

fun Bindings.definition(script: Script): Definition =
	null
		?: doesDefinitionOrNull(script)
		?: givesDefinitionOrNull(script)
		?: error("definition parse error")

fun Bindings.doesDefinitionOrNull(script: Script): Definition? =
	script.matchInfix("does") { lhs, rhs ->
		lhs.type.let { lhsType ->
			push(lhsType).typed(rhs).let { typed ->
				definition(fn(typed.term).of(lhsType arrowTo typed.type))
			}
		}
	}

fun Bindings.givesDefinitionOrNull(script: Script): Definition? =
	script.matchInfix("gives") { lhs, rhs ->
		definition(lhs.type, typed(rhs))
	}

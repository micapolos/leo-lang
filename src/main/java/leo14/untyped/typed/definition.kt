package leo14.untyped.typed

import leo.base.The
import leo14.lambda.runtime.Value

sealed class Definition

data class BindingDefinition(val binding: Binding) : Definition()
data class RuleDefinition(val rule: Rule) : Definition()

fun definition(binding: Binding): Definition = BindingDefinition(binding)
fun definition(rule: Rule): Definition = RuleDefinition(rule)

fun Definition.apply(typed: Compiled<*>): Compiled<*>? =
	TODO()
//	when (this) {
//		is BindingDefinition -> binding.apply(typed)
//		is RuleDefinition -> rule.apply(typed)
//	}

fun Definition.applyValue(value: Value): The<Value>? =
	when (this) {
		is BindingDefinition -> applyValue(value)
		is RuleDefinition -> applyValue(value)
	}

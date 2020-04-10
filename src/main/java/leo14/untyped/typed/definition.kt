package leo14.untyped.typed

sealed class Definition

data class BindingDefinition(val binding: Binding) : Definition()
data class RuleDefinition(val rule: Rule) : Definition()

fun definition(binding: Binding): Definition = BindingDefinition(binding)
fun definition(rule: Rule): Definition = RuleDefinition(rule)

fun Definition.apply(compiled: Compiled): Compiled? =
	when (this) {
		is BindingDefinition -> binding.apply(compiled)
		is RuleDefinition -> rule.apply(compiled)
	}

fun Compiled.definitionOrNull(scope: Scope): Definition? =
	null
		?: bindingOrNull?.let(::definition)
		?: ruleOrNull(scope)?.let(::definition)


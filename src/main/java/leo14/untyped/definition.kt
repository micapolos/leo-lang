package leo14.untyped

sealed class Definition

data class RuleDefinition(val rule: Rule) : Definition()
data class BindingDefinition(val binding: Binding) : Definition()
data class RecurseDefinition(val recurse: Recurse) : Definition()

fun definition(rule: Rule): Definition = RuleDefinition(rule)
fun definition(binding: Binding): Definition = BindingDefinition(binding)
fun definition(recurse: Recurse): Definition = RecurseDefinition(recurse)

fun Definition.apply(scope: Scope, thunk: Thunk): Applied? =
	when (this) {
		is RuleDefinition -> rule.apply(scope, thunk)
		is BindingDefinition -> binding.apply(thunk)?.let(::applied)
		is RecurseDefinition -> recurse.apply(thunk)?.let(::applied)
	}

val Thunk.parseDefinition: Definition?
	get() =
		parseBinding?.let(::definition)
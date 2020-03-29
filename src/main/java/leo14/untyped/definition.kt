package leo14.untyped

sealed class Definition

data class RuleDefinition(val rule: Rule) : Definition()

fun definition(rule: Rule): Definition = RuleDefinition(rule)

fun Definition.apply(context: Context, thunk: Thunk): Applied? =
	when (this) {
		is RuleDefinition -> rule.apply(context, thunk)
	}
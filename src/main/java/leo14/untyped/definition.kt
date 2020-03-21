package leo14.untyped

sealed class Definition

data class RuleDefinition(val rule: Rule) : Definition()
data class RecursiveDefinition(val recursive: Recursive) : Definition()

fun definition(rule: Rule): Definition = RuleDefinition(rule)
fun definition(recursive: Recursive): Definition = RecursiveDefinition(recursive)

fun Definition.apply(context: Context, program: Program): Program? =
	when (this) {
		is RuleDefinition -> rule.apply(context, program)
		is RecursiveDefinition -> recursive.apply(context, program)
	}
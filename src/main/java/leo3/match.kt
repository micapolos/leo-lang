package leo3

sealed class Match
data class ScopeMatch(val scope: Scope) : Match()
data class TemplateMatch(val template: Template) : Match()

fun match(scope: Scope): Match = ScopeMatch(scope)
fun match(template: Template): Match = TemplateMatch(template)

fun Match.resolve(scope: Scope, buffer: Buffer): Interpreter =
	when (this) {
		is ScopeMatch -> Interpreter(scope, Evaluator(buffer, this.scope))
		is TemplateMatch -> TODO()
	}

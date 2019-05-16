package leo3

sealed class Match
data class FunctionMatch(val function: Function) : Match()
data class TemplateMatch(val template: Template) : Match()
data class CallMatch(val call: Call) : Match()

fun match(function: Function): Match = FunctionMatch(function)
fun match(template: Template): Match = TemplateMatch(template)
fun match(call: Call): Match = CallMatch(call)

fun Match.resolve(tokenReader: TokenReader): Invocation? =
	when (this) {
		is FunctionMatch -> Invocation(tokenReader, function)
		is TemplateMatch -> tokenReader.parameterOrNull?.let { parameter ->
			template.apply(parameter)?.termOrNull.run { invocation(this) }
		}
		is CallMatch -> TODO()
	}

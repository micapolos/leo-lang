package leo3

sealed class Match
data class FunctionMatch(val function: Function) : Match()
data class TemplateMatch(val template: Template) : Match()
data class CallMatch(val call: Call) : Match()

fun match(function: Function): Match = FunctionMatch(function)
fun match(template: Template): Match = TemplateMatch(template)
fun match(call: Call): Match = CallMatch(call)

fun Match.resolve(termParser: TermParser): Value? =
	when (this) {
		is FunctionMatch -> Value(termParser, function)
		is TemplateMatch -> termParser.parameterOrNull?.let { parameter ->
			template.apply(parameter)?.termOrNull.run { value(this) }
		}
		is CallMatch -> TODO()
	}

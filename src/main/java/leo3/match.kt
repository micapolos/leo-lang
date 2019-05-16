package leo3

sealed class Match
data class FunctionMatch(val function: Function) : Match()
data class BodyMatch(val body: Body) : Match()
data class CallMatch(val call: Call) : Match()

fun match(function: Function): Match = FunctionMatch(function)
fun match(body: Body): Match = BodyMatch(body)
fun match(call: Call): Match = CallMatch(call)

fun Match.resolve(script: Script): Value? =
	when (this) {
		is FunctionMatch -> Value(script, function)
		is BodyMatch -> script.parameterOrNull.let { parameter ->
			template.apply(parameter).termOrNull.run { value(this) }
		}
		is CallMatch -> TODO()
	}

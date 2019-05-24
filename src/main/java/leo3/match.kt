package leo3

sealed class Match
data class FunctionMatch(val function: Function) : Match()
data class BodyMatch(val body: Body) : Match()

fun match(function: Function): Match = FunctionMatch(function)
fun match(body: Body): Match = BodyMatch(body)

val Match.functionOrNull get() = (this as? FunctionMatch)?.function
val Match.bodyOrNull get() = (this as? BodyMatch)?.body

fun Writer.writePattern(match: Match): Writer =
	when (match) {
		is FunctionMatch -> write0.writePattern(match.function)
		is BodyMatch -> write1
	}

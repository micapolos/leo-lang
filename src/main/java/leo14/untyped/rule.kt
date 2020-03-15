package leo14.untyped

data class Rule(val pattern: Pattern, val body: Body)

infix fun Pattern.ruleTo(body: Body) = Rule(this, body)

fun Rule.apply(program: Program): Program? =
	if (pattern.matches(program)) body.apply(program)
	else null

val Program.givenRule
	get() =
		givenPattern ruleTo body(make("given"))

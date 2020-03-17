package leo14.untyped

import leo13.thisName

data class Rule(val pattern: Pattern, val body: Body)

infix fun Pattern.ruleTo(body: Body) = Rule(this, body)
fun rule(pattern: Pattern, body: Body) = Rule(pattern, body)

fun Rule.apply(program: Program): Program? =
	when (body) {
		is ProgramBody ->
			if (pattern.program == program) body.apply(program)
			else null
		is FunctionBody ->
			if (pattern.matches(program)) body.apply(program)
			else null
	}


val Program.thisRule
	get() =
		thisPattern ruleTo body(make(thisName))

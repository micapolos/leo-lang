package leo14.untyped

import leo.base.notNullIf

data class Rule(val pattern: Pattern, val body: Body)

infix fun Pattern.ruleTo(body: Body) = Rule(this, body)
fun rule(pattern: Pattern, body: Body) = Rule(pattern, body)

fun Rule.apply(context: Context, program: Program): Program? =
	notNullIf(pattern.matches(program)) {
		body.apply(context, program)
	}

val Function.recurseRule
	get() =
		recursePattern ruleTo body(program(value(this)))

val Program.givenRule
	get() =
		rule(
			pattern(program("given")),
			body(program("given" valueTo this)))
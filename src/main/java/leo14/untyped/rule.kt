package leo14.untyped

import leo.base.notNullIf

data class Rule(val pattern: Pattern, val body: Body)

fun rule(pattern: Pattern, body: Body) = Rule(pattern, body)

fun Rule.apply(context: Context, program: Program): Thunk? =
	notNullIf(pattern.matches(program)) {
		body.apply(context, program)
	}

val Function.recurseRule
	get() =
		rule(recursePattern, body(program(value(this))))

val Program.givenRule
	get() =
		rule(
			pattern(program(givenName)),
			body(program(givenName valueTo this)))
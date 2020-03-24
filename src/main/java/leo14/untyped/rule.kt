package leo14.untyped

import leo.base.notNullIf

data class Rule(val pattern: Pattern, val body: Body)

fun rule(pattern: Pattern, body: Body) = Rule(pattern, body)

fun Rule.apply(context: Context, given: Thunk): Thunk? =
	notNullIf(pattern.matches(given)) {
		body.apply(context, given)
	}

val Function.recurseRule
	get() =
		rule(recursePattern, body(thunk(value(line(this)))))

val Thunk.givenRule
	get() =
		rule(
			pattern(thunk(value(givenName))),
			body(thunk(value(givenName lineTo this))))

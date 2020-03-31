package leo14.untyped

import leo.base.notNullIf

data class Rule(val pattern: Pattern, val body: Body)

fun rule(pattern: Pattern, body: Body) = Rule(pattern, body)

fun Rule.apply(scope: Scope, given: Thunk): Applied? =
	when (body) {
		is ThunkBody -> notNullIf(pattern.thunk == given) {
			body.apply(scope, given)
		}
		else -> notNullIf(pattern.matches(given)) {
			body.apply(scope, given)
		}

	}

val Thunk.givenRule
	get() =
		rule(
			pattern(thunk(value(givenName))),
			body(thunk(value(givenName lineTo this))))

package leo14.untyped

import leo.base.notNullIf

data class Rule(val pattern: Pattern, val body: Body)

infix fun Pattern.ruleTo(body: Body) = Rule(this, body)
fun rule(pattern: Pattern, body: Body) = Rule(pattern, body)

fun Rule.apply(program: Program): Program? =
	notNullIf(pattern.matches(program)) {
		body.apply(program)
	}

val Function.recurseRule
	get() =
		recursePattern ruleTo body(constant(program(value(this))))

val Program.givenRule
	get() =
		rule(
			pattern(program("given")),
			program("given" valueTo this).constant.body)
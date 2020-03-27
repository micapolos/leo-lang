package leo14.lambda.scheme

import leo.base.string
import leo14.Literal
import leo14.lambda.*
import leo14.lambda.code.Gen
import leo14.lambda.code.gen
import leo14.lambda.code.inc

val Term.code get() = code(gen)

fun Term.code(gen: Gen): Code =
	when (this) {
		is NativeTerm -> native
		is AbstractionTerm -> abstraction.code(gen)
		is ApplicationTerm -> application.code(gen)
		is VariableTerm -> variable.code(gen)
	}

fun Abstraction<Term>.code(gen: Gen) = paramCode(gen) ret gen.inc { body.code(it) }
fun Application<Term>.code(gen: Gen) = lhs.code(gen).ap(rhs.code(gen))
fun Variable<Code>.code(gen: Gen) = index(gen).varCode

val Literal.code: Code
	get() = code(string)

package leo14.lambda.js.expr

import leo13.int
import leo14.code.Code
import leo14.code.code
import leo14.lambda.*
import leo14.lambda.code.Gen
import leo14.lambda.code.gen
import leo14.lambda.code.inc

val Term<Expr>.code: Code
	get() =
		code(gen)

fun Term<Expr>.code(gen: Gen): Code =
	when (this) {
		is NativeTerm -> native.code(gen)
		is AbstractionTerm -> abstraction.code(gen)
		is ApplicationTerm -> application.code(gen)
		is VariableTerm -> variable.code(gen)
	}

fun Abstraction<Term<Expr>>.code(gen: Gen): Code =
	"${gen.depth.variableCode}=>${gen.inc { body.code(gen) }}".code

fun Application<Term<Expr>>.code(gen: Gen): Code =
	"(${lhs.code(gen)})(${rhs.code(gen)})".code

fun Variable<Expr>.code(gen: Gen): Code =
	gen.depth.minus(index.int).variableCode

val Int.variableCode: Code
	get() =
		"v$this".code
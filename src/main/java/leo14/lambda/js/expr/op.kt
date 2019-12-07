package leo14.lambda.js.expr

import leo14.code.Code
import leo14.code.code
import leo14.lambda.Term
import leo14.lambda.code.Gen

data class Op(val lhs: Term<Expr>, val name: String, val rhs: Term<Expr>)

fun Term<Expr>.op(name: String, rhs: Term<Expr>) = Op(this, name, rhs)

fun Op.code(gen: Gen): Code =
	"(${lhs.code(gen)})$name(${rhs.code(gen)})".code
package lambda.v2

import leo.base.int

fun varString(index: Int) =
	if (index < 0) "?${-index}"
	else "x$index"

fun Argument.string(argCount: Int) =
	varString(argCount - nat.int - 1)

fun Application.string(argCount: Int) =
	"${lhs.string(argCount)}(${rhs.string(argCount)})"

fun Function.string(argCount: Int) =
	"${varString(argCount)} -> ${term.string(argCount.inc())}"

fun Quote.string(argCount: Int) =
	"quote"

fun Unquote.string(argCount: Int) =
	"unquote"

fun Term.string(argCount: Int = 0): String = when (this) {
	is ArgumentTerm -> argument.string(argCount)
	is ApplicationTerm -> application.string(argCount)
	is FunctionTerm -> function.string(argCount)
	is QuoteTerm -> quote.string(argCount)
	is UnquoteTerm -> unquote.string(argCount)
}

package lambda.v2

import leo.base.int

val Argument.string
	get() =
		"a${nat.int}"

val Application.string
	get() =
		"${lhs.string}(${rhs.string})"

val Function.string
	get() =
		"fn { ${body.term.string} }"

val Quote.string
	get() =
	"quote"

val Unquote.string
	get() =
	"unquote"

val Term.string: String
	get() = when (this) {
		is ArgumentTerm -> argument.string
		is ApplicationTerm -> application.string
		is FunctionTerm -> function.string
		is QuoteTerm -> quote.string
		is UnquoteTerm -> unquote.string
}

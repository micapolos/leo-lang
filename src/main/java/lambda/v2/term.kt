package lambda.v2

sealed class Term {
	override fun toString() = string(0)
}

data class ArgumentTerm(val argument: Argument) : Term() {
	override fun toString() = super.toString()
}

data class ApplicationTerm(val application: Application) : Term() {
	override fun toString() = super.toString()
}

data class FunctionTerm(val function: Function) : Term() {
	override fun toString() = super.toString()
}

data class QuoteTerm(val quote: Quote) : Term() {
	override fun toString() = super.toString()
}

data class UnquoteTerm(val unquote: Unquote) : Term() {
	override fun toString() = super.toString()
}

fun term(argument: Argument): Term = ArgumentTerm(argument)
fun term(application: Application): Term = ApplicationTerm(application)
fun term(function: Function): Term = FunctionTerm(function)
fun term(quote: Quote): Term = QuoteTerm(quote)
fun term(unquote: Unquote): Term = UnquoteTerm(unquote)

val Term.argumentOrNull get() = (this as? ArgumentTerm)?.argument
val Term.applicationOrNull get() = (this as? ApplicationTerm)?.application
val Term.functionOrNull get() = (this as? FunctionTerm)?.function
val Term.quoteOrNull get() = (this as? QuoteTerm)?.quote
val Term.unquoteOrNull get() = (this as? UnquoteTerm)?.unquote

fun Term.apply(term: Term) = term(application(this, term))

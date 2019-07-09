package lambda.v2

import leo.base.Stack
import leo.base.get
import leo.base.push

fun Argument.evalOrNull(trace: Stack<Term>?) =
	trace?.get(nat)

fun Application.evalOrNull(trace: Stack<Term>?) =
	lhs.eval(trace).let { lhsEval ->
		rhs.eval(trace).let { rhsEval ->
			when (lhsEval) {
				is ArgumentTerm -> null
				is ApplicationTerm -> null
				is FunctionTerm -> lhsEval.function.term.eval(trace.push(rhsEval))
				is QuoteTerm -> rhsEval.quotedTerm
				is UnquoteTerm -> rhsEval.unquoteTerm
			}
		}
	}

fun Function.evalOrNull(trace: Stack<Term>?) = null as Term?

fun Quote.evalOrNull(trace: Stack<Term>?) = null as Term?

fun Unquote.evalOrNull(trace: Stack<Term>?) = null as Term?

fun Term.eval(trace: Stack<Term>?): Term =
	when (this) {
		is ArgumentTerm -> argument.evalOrNull(trace)
		is ApplicationTerm -> application.evalOrNull(trace)
		is FunctionTerm -> function.evalOrNull(trace)
		is QuoteTerm -> quote.evalOrNull(trace)
		is UnquoteTerm -> unquote.evalOrNull(trace)
	} ?: this

val Term.eval get() = eval(null)

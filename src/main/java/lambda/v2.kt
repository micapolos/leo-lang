package lambda

import lambda.v2.argument
import lambda.v2.body
import leo.base.Stack
import leo.base.natOrNull
import leo.base.push

fun Variable.v2(trace: Stack<Variable>?) =
	trace?.natOrNull(this)!!.argument

fun Application.v2(trace: Stack<Variable>?) =
	lambda.v2.application(left.v2(trace), right.v2(trace))

fun Function.v2(trace: Stack<Variable>?) =
	newVariable.let { variable ->
		lambda.v2.function(body(fn(term(variable)).v2(trace.push(variable))))
	}

fun Term.v2(trace: Stack<Variable>?): lambda.v2.Term =
	when (this) {
		is VariableTerm -> lambda.v2.term(variable.v2(trace))
		is ApplicationTerm -> lambda.v2.term(application.v2(trace))
		is FunctionTerm -> lambda.v2.term(function.v2(trace))
	}

val Term.v2 get() = v2(null)

package lambda

import lambda.indexed.argument
import lambda.indexed.body
import leo.base.Stack
import leo.base.natOrNull
import leo.base.push

fun Variable.indexed(trace: Stack<Variable>?) =
	trace?.natOrNull(this)!!.argument

fun Application.indexed(trace: Stack<Variable>?) =
	lambda.indexed.application(left.indexed(trace), right.indexed(trace))

fun Function.indexed(trace: Stack<Variable>?) =
	newVariable.let { variable ->
		lambda.indexed.function(body(fn(term(variable)).indexed(trace.push(variable))))
	}

fun Term.indexed(trace: Stack<Variable>?): lambda.indexed.Term =
	when (this) {
		is VariableTerm -> lambda.indexed.term(variable.indexed(trace))
		is ApplicationTerm -> lambda.indexed.term(application.indexed(trace))
		is FunctionTerm -> lambda.indexed.term(function.indexed(trace))
	}

val Term.v2 get() = indexed(null)

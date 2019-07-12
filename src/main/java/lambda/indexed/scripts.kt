package lambda.indexed

import lambda.script.variable
import leo.base.*

val Nat.scriptVariable get() = variable("x$this")

// TODO: Support free variables!!!
fun Argument.script(depth: Nat): lambda.script.Variable =
	depth.minusOrNull(nat)!!.scriptVariable

fun Application.script(depth: Nat): lambda.script.Application =
	lambda.script.application(lhs.script(depth), rhs.script(depth))

fun Function.script(depth: Nat): lambda.script.Function =
	lambda.script.function(depth.scriptVariable, body.term.script(depth.inc))

// TODO: Support quote and unquote!!!
fun Term.script(depth: Nat): lambda.script.Term =
	when (this) {
		is ArgumentTerm -> lambda.script.term(argument.script(depth))
		is ApplicationTerm -> lambda.script.term(application.script(depth))
		is FunctionTerm -> lambda.script.term(function.script(depth))
		is QuoteTerm -> fail()
		is UnquoteTerm -> fail()
	}

val Term.script get() = script(leo.binary.zero.nat)
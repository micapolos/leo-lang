package leo19.term.eval

import leo13.get
import leo13.map
import leo13.toList
import leo19.term.ArrayGetTerm
import leo19.term.ArrayTerm
import leo19.term.FunctionTerm
import leo19.term.IntTerm
import leo19.term.InvokeTerm
import leo19.term.NullTerm
import leo19.term.Term
import leo19.term.VariableTerm
import leo19.term.nullTerm
import leo19.term.term

val Term.eval get() = emptyScope.eval(this)

fun Scope.eval(term: Term): Value =
	when (term) {
		NullTerm -> NullValue
		is IntTerm -> value(term.int)
		is ArrayTerm -> ArrayValue(term.stack.map { eval(this) }.toList())
		is ArrayGetTerm -> (eval(term.tuple) as ArrayValue).list[(eval(term.index) as IntValue).int]
		is FunctionTerm -> value(function(this, term.function.body))
		is InvokeTerm -> (eval(term.function) as FunctionValue).function.let { function ->
			function.scope.push(eval(term.param)).eval(function.body)
		}
		is VariableTerm -> stack.get(term.variable.index)!!
	}

val Value.term: Term
	get() =
		when (this) {
			NullValue -> nullTerm
			is IntValue -> term(int)
			is ArrayValue -> term(*list.map { it.term }.toTypedArray())
			is FunctionValue -> null
		}!!
package leo19.term.eval

import leo13.get
import leo19.term.FunctionTerm
import leo19.term.IntTerm
import leo19.term.InvokeTerm
import leo19.term.Term
import leo19.term.TupleGetTerm
import leo19.term.TupleTerm
import leo19.term.VariableTerm

val Term.eval get() = emptyScope.eval(this)

fun Scope.eval(term: Term): Value =
	when (term) {
		is IntTerm -> IntValue(term.int)
		is TupleTerm -> TupleValue(term.list.map { eval(it) })
		is TupleGetTerm -> (eval(term.tuple) as TupleValue).list[(eval(term.index) as IntValue).int]
		is FunctionTerm -> FunctionValue(this, term.body)
		is InvokeTerm -> (eval(term.function) as FunctionValue).let { function ->
			function.scope.push(eval(term.param)).eval(function.body)
		}
		is VariableTerm -> stack.get(term.index)!!
	}
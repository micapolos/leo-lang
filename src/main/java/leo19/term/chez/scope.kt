package leo19.term.chez

import leo19.term.FunctionTerm
import leo19.term.IntTerm
import leo19.term.InvokeTerm
import leo19.term.Term
import leo19.term.ArrayGetTerm
import leo19.term.ArrayTerm
import leo19.term.VariableTerm

data class Scope(val variableCount: Int)

val emptyScope = Scope(0)
val Scope.push get() = copy(variableCount = variableCount.inc())

fun Scope.string(term: Term): String =
	when (term) {
		is IntTerm -> "${term.int}"
		is ArrayTerm -> "(vector ${term.list.joinToString(" ") { string(it) }})"
		is ArrayGetTerm -> "(vector-ref ${string(term.tuple)} ${string(term.index)})"
		is FunctionTerm -> "(lambda (v${variableCount}) ${push.string(term.function.body)})"
		is InvokeTerm -> "(${string(term.function)} ${string(term.param)})"
		is VariableTerm -> "v${variableCount - term.variable.index - 1}"
	}

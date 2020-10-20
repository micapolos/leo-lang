package leo19.term.chez

import leo.base.runIf
import leo.base.updateIfNotNull
import leo13.lhs
import leo13.toList
import leo15.lambda.runtime.builder.term
import leo19.term.ArrayGetTerm
import leo19.term.ArrayTerm
import leo19.term.EqualsTerm
import leo19.term.FunctionTerm
import leo19.term.IntTerm
import leo19.term.InvokeTerm
import leo19.term.LhsTerm
import leo19.term.NullTerm
import leo19.term.PairTerm
import leo19.term.RhsTerm
import leo19.term.Term
import leo19.term.VariableTerm

data class Scope(val variableCount: Int)

val emptyScope = Scope(0)
val Scope.push get() = copy(variableCount = variableCount.inc())

fun Scope.string(term: Term): String =
	when (term) {
		NullTerm -> "'()"
		is IntTerm -> "${term.int}"
		is PairTerm -> "(cons ${string(term.lhs)} ${string(term.rhs)})"
		is LhsTerm -> "(car ${string(term.pair)})"
		is RhsTerm -> "(cdr ${string(term.pair)})"
		is ArrayTerm -> "(vector ${term.stack.toList().joinToString(" ") { string(it) }})"
		is ArrayGetTerm -> "(vector-ref ${string(term.tuple)} ${string(term.index)})"
		is FunctionTerm ->
			if (!term.function.isRecursive) "(lambda (v${variableCount}) ${push.string(term.function.body)})"
			else "(letrec ((v${variableCount} (lambda (v${variableCount.inc()}) ${push.push.string(term.function.body)}))) v${variableCount})"
		is InvokeTerm -> "(${string(term.function)} ${string(term.param)})"
		is VariableTerm -> "v${variableCount - term.variable.index - 1}"
		is EqualsTerm -> "(equal? ${term.lhs} ${term.rhs})"
	}

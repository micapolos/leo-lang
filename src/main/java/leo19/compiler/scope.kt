package leo19.compiler

import leo.base.fold
import leo.base.reverse
import leo13.Stack
import leo13.get
import leo13.push
import leo13.seq
import leo13.stack
import leo14.reflectOrEmptyScriptLine
import leo19.term.Term
import leo19.term.function
import leo19.term.invoke
import leo19.term.reflect

data class Scope(val termStack: Stack<Term>)

val Scope.reflect get() = termStack.reflectOrEmptyScriptLine("scope") { reflect }

fun scope(vararg terms: Term) = Scope(stack(*terms))
fun Scope.plus(term: Term) = Scope(termStack.push(term))
fun Scope.get(index: Int) = termStack.get(index)!!

fun Scope.wrap(term: Term) =
	term
		.fold(termStack.seq) { leo19.term.term(function(it)) }
		.fold(termStack.seq.reverse) { invoke(it) }

@file:Suppress("unused")

package leo32.term

import leo.base.Empty
import leo32.base.Dict
import leo32.base.at
import leo32.base.put

data class TermResolver(
	val termToTermDict: Dict<Term, Term>)

val Dict<Term, Term>.termResolver get() =
	TermResolver(this)

val Empty.termResolver get() =
	termDict<Term>().termResolver

fun TermResolver.put(key: Term, value: Term) =
	copy(termToTermDict = termToTermDict.put(key, value))

fun TermResolver.resolve(term: Term): Term =
	termToTermDict.at(term)!!
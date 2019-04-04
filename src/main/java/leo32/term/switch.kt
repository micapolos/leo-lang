@file:Suppress("unused")

package leo32.term

import leo.base.Empty
import leo32.base.Dict
import leo32.base.at
import leo32.base.put

data class Switch(
	val termToTermDict: Dict<Term, Term>)

val Dict<Term, Term>.switch get() =
	Switch(this)

val Empty.switch get() =
	termDict<Term>().switch

fun Switch.put(key: Term, value: Term) =
	copy(termToTermDict = termToTermDict.put(key, value))

fun Switch.resolve(term: Term): Term =
	termToTermDict.at(term)!!
@file:Suppress("unused")

package leo32.runtime

import leo.base.Empty
import leo.base.empty
import leo.base.fold
import leo32.base.Dict
import leo32.base.at
import leo32.base.put

data class Switch(
	val termToTermDict: Dict<Term, Term>)

val Dict<Term, Term>.switch get() =
	Switch(this)

val Empty.switch get() =
	termDict<Term>().switch

fun switch(vararg cases: Case) =
	empty.switch.fold(cases) { plus(it) }

fun Switch.plus(case: Case) =
	copy(termToTermDict = termToTermDict.put(case.key, case.value))

fun Switch.invoke(term: Term): Term =
	termToTermDict
		.at(term)
		?: term("error" to
			term(
				termField,
				"invoke" to term))

val Switch.termField
	get() =
		"switch" to term()

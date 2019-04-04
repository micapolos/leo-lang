@file:Suppress("unused")

package leo32.interpreter

import leo.base.Empty
import leo.base.empty
import leo32.base.Dict
import leo32.base.at
import leo32.base.put
import leo32.runtime.Term
import leo32.runtime.map
import leo32.runtime.termDict

data class Types(
	val termToTypeDict: Dict<Term, Type>)

val Dict<Term, Type>.types get() =
	Types(this)

val Empty.types get() =
	empty.termDict<Type>().types

fun Types.put(term: Term, type: Type) =
	copy(termToTypeDict = termToTypeDict.put(term, type))

fun Types.typeOf(term: Term): Type =
	term.map { typeOf(this).term }.let { mappedTerm ->
		termToTypeDict.at(mappedTerm) ?: type(mappedTerm)
	}

@file:Suppress("unused")

package leo32.runtime

import leo.base.Empty
import leo.base.empty
import leo32.base.Dict
import leo32.base.at
import leo32.base.put

data class Types(
	val termToTypeDict: Dict<Term, Type>)

val Dict<Term, Type>.types get() =
	Types(this)

val Empty.types get() =
	empty.termDict<Type>().types

fun Types.put(term: Term, Type: Type) =
	copy(termToTypeDict = termToTypeDict.put(term, Type))

fun Types.at(term: Term): Type =
	term.map { at(this).term }.let { mappedTerm ->
		termToTypeDict.at(mappedTerm) ?: mappedTerm.parseType
	}

@file:Suppress("unused")

package leo32.interpreter

import leo.base.Empty
import leo.base.empty
import leo32.base.Dict
import leo32.base.at
import leo32.base.put
import leo32.runtime.Term
import leo32.runtime.resolve
import leo32.runtime.termDict

data class TypeResolver(
	val termToTypeDict: Dict<Term, Type>)

val Dict<Term, Type>.typeResolver get() =
	TypeResolver(this)

val Empty.typeResolver get() =
	empty.termDict<Type>().typeResolver

fun TypeResolver.put(term: Term, type: Type) =
	copy(termToTypeDict = termToTypeDict.put(term, type))

fun TypeResolver.resolve(term: Term) =
	term.resolve { termToTypeDict.at(this)?.term?:this }.type
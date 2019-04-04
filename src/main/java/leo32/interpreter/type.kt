package leo32.interpreter

import leo.base.Empty
import leo32.base.dict
import leo32.base.dictKey
import leo32.runtime.Term
import leo32.runtime.seq32

data class Type(
	val term: Term)

val Term.type get() =
	Type(this)

fun type(term: Term) =
	Type(term)

val Type.seq32 get() =
	term.seq32

val Type.dictKey get() =
	seq32.dictKey

fun <V: Any> Empty.typeDict() =
	dict<Type, V> { dictKey }
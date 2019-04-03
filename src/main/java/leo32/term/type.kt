package leo32.term

import leo.base.Empty
import leo32.base.dict
import leo32.base.dictKey

data class Type(
	val term: Term)

val Term.type get() =
	Type(this)

val Type.seq32 get() =
	term.seq32

val Type.dictKey get() =
	seq32.dictKey

fun <V: Any> Empty.typeDict() =
	dict<Type, V> { dictKey }
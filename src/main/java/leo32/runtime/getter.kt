package leo32.runtime

import leo32.base.I32
import leo32.base.only

sealed class Getter

data class NameGetter(
	val name: String): Getter()

data class IndexGetter(
	val index: I32): Getter()

val String.getter get() =
	NameGetter(this) as Getter

val I32.getter get() =
	IndexGetter(this) as Getter

fun Getter.invoke(term: Term): Term =
	when (this) {
		is NameGetter -> term.at(name).only
		is IndexGetter -> term.at(index).value
	}

package leo32.term

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

fun Term.invoke(getter: Getter): Term =
	when (getter) {
		is NameGetter -> at(getter.name).only
		is IndexGetter -> at(getter.index).value
	}

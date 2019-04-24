package leo32.runtime

import leo.base.Empty
import leo.base.Seq
import leo.binary.Bit

data class Set<K>(
	val unitDict: Dict<K, Unit>)

val <K> Dict<K, Unit>.set
	get() =
		Set(this)

fun <K> Empty.set(fn: K.() -> Seq<Bit>) =
	dict<K, Unit>(fn)

fun <K> Set<K>.add(value: K) =
	unitDict.put(value, Unit).set

fun <K> Set<K>.contains(value: K) =
	unitDict.at(value) != null

val Empty.symbolSet
	get() =
		symbolDict<Unit>().set
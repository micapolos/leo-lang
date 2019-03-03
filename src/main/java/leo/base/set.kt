package leo.base

import leo.binary.Bit

data class Set<V>(
	val unitMap: Map<V, Unit>)

val <V> Map<V, Unit>.set
	get() =
		Set(this)

fun <V> emptySet(bitStreamOrNullFn: (V) -> Stream<Bit>?): Set<V> =
	emptyMap<V, Unit>(bitStreamOrNullFn).set

fun <V> Set<V>.contains(value: V): Boolean =
	unitMap.get(value) != null

fun <V> Set<V>.add(value: V): Set<V> =
	unitMap.set(value, Unit).set

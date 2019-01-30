package leo

sealed class Definable<out V>

data class DefinedDefinable<out V>(
	val defined: Defined<V>) : Definable<V>()

data class NotDefinedDefinable<out V>(
	val notDefined: Not<Defined<V>>) : Definable<V>()

fun <V> definable(defined: Defined<V>) =
	DefinedDefinable(defined)

fun <V> definable(notDefined: Not<Defined<V>>) =
	NotDefinedDefinable(notDefined)

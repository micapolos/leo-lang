package leo13.base

data class Option<out V : Any>(val orNull: V?)

fun <V : Any> option(orNull: V? = null): Option<V> = Option(orNull)

fun <V : Any, R : Any> Option<V>.map(fn: V.() -> R): Option<R> =
	option(orNull?.fn())

fun <V : Any, R : Any> Option<V>.optionMap(fn: V.() -> Option<R>): Option<R> =
	orNull?.fn() ?: option(null)

//fun <V: Any> optionType(orNullType: Type<V>): Type<Option<V>> =
//	type<Option<V>>(
//		orNullType.name,
//		body(typeOption(orNullType)))

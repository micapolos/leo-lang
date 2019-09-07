package leo13.base

import leo13.script.reflect.Type
import leo13.script.reflect.scriptLine

data class Option<V : Any>(val type: Type<V>, val orNull: V?) {
	override fun toString() = type.scriptLine(this).toString()
}

fun <V : Any> option(type: Type<V>, orNull: V? = null): Option<V> = Option(type, orNull)

fun <V : Any, R : Any> Option<V>.map(type: Type<R>, fn: V.() -> R): Option<R> =
	option(type, orNull?.fn())

fun <V : Any, R : Any> Option<V>.optionMap(type: Type<R>, fn: V.() -> Option<R>): Option<R> =
	orNull?.fn() ?: option(type, null)

package leo13.base

import leo13.script.lineTo
import leo13.script.script
import leo13.script.unsafeOnlyLine
import leo13.script.unsafeRhs
import leo13.scripter.Scripter
import leo13.scripter.scriptLine
import leo13.scripter.toString
import leo13.scripter.unsafeValue

fun <V : Any> optionScripter(orNullType: Scripter<V>): Scripter<Option<V>> =
	Scripter(
		orNullType.name,
		{
			script(
				"option" lineTo script(
					if (orNull == null) "null" lineTo script()
					else orNullType.scriptLine(orNull)))
		},
		{
			unsafeOnlyLine
				.unsafeRhs("option")
				.let { rhs ->
					if (rhs == script("null")) option(orNullType)
					else option(orNullType, orNullType.unsafeValue(this))
				}
		})

data class Option<V : Any>(val orNullScripter: Scripter<V>, val orNull: V?) {
	override fun toString() = optionScripter(orNullScripter).toString(this)
}

fun <V : Any> option(type: Scripter<V>, orNull: V? = null): Option<V> = Option(type, orNull)

fun <V : Any, R : Any> Option<V>.map(type: Scripter<R>, fn: V.() -> R): Option<R> =
	option(type, orNull?.fn())

fun <V : Any, R : Any> Option<V>.optionMap(type: Scripter<R>, fn: V.() -> Option<R>): Option<R> =
	orNull?.fn() ?: option(type, null)

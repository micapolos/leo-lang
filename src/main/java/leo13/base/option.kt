package leo13.base

import leo13.base.type.Type
import leo13.base.type.scriptLine
import leo13.base.type.toString
import leo13.base.type.unsafeValue
import leo13.script.lineTo
import leo13.script.script
import leo13.script.unsafeOnlyLine
import leo13.script.unsafeRhs

fun <V : Any> optionType(orNullType: Type<V>): Type<Option<V>> =
	Type(
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

data class Option<V : Any>(val orNullType: Type<V>, val orNull: V?) {
	override fun toString() = optionType(orNullType).toString(this)
}

fun <V : Any> option(type: Type<V>, orNull: V? = null): Option<V> = Option(type, orNull)

fun <V : Any, R : Any> Option<V>.map(type: Type<R>, fn: V.() -> R): Option<R> =
	option(type, orNull?.fn())

fun <V : Any, R : Any> Option<V>.optionMap(type: Type<R>, fn: V.() -> Option<R>): Option<R> =
	orNull?.fn() ?: option(type, null)

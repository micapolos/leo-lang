package leo13.script.reflect

import leo.base.notNullIf
import leo13.base.option
import leo13.script.*
import leo13.base.Option as ValueOption

data class Option<V : Any>(val orNullType: Type<V>)

fun <V : Any> option(orNullType: Type<V>) = Option(orNullType)

fun <V : Any> Option<V>.unsafeBodyValue(script: Script): ValueOption<V> =
	script
		.unsafeOnlyLine
		.unsafeRhs("option")
		.let { rhs ->
			option(
				notNullIf(rhs != script("null")) {
					orNullType.unsafeValue(rhs)
				}
			)
		}

fun <V : Any> Option<V>.bodyScript(value: ValueOption<V>): Script =
	script(
		"option" lineTo script(
			if (value.orNull == null) "null" lineTo script()
			else orNullType.scriptLine(value.orNull)))

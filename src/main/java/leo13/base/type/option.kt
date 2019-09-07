package leo13.base.type

import leo.base.notNullIf
import leo13.base.option
import leo13.script.*
import leo13.base.Option as ValueOption

data class TypeOption<V : Any>(val orNullType: Type<V>)

fun <V : Any> option(orNullType: Type<V>) = TypeOption(orNullType)

fun <V : Any> TypeOption<V>.unsafeBodyValue(script: Script): ValueOption<V> =
	script
		.unsafeOnlyLine
		.unsafeRhs("option")
		.let { rhs ->
			option(
				orNullType,
				notNullIf(rhs != script("null")) {
					orNullType.unsafeValue(rhs)
				}
			)
		}

fun <V : Any> TypeOption<V>.bodyScript(value: ValueOption<V>): Script =
	script(
		"option" lineTo script(
			if (value.orNull == null) "null" lineTo script()
			else orNullType.scriptLine(value.orNull)))

package leo14.untyped.typed

import leo14.Number
import leo14.lambda.runtime.Value
import leo14.number
import leo14.untyped.*

val selfType: Value = selfName
val textType: Value = textName
val intType: Value = intName
val booleanType: Value = booleanName
val numberType: Value = numberName
val nativeType: Value = nativeName

fun Typed.cast(targetType: Value): Typed? =
	if (type == targetType) this
	else castValueFn(targetType)?.let { typed(targetType, it) }

fun Typed.castValueFn(targetType: Value): ValueFn? =
	when (targetType) {
		intType ->
			when (type) {
				selfType ->
					when (value) {
						is Int -> valueFn
						else -> null
					}
				else -> null
			}
		textType ->
			when (type) {
				selfType ->
					when (value) {
						is String -> valueFn
						else -> null
					}
				else -> null
			}
		numberType ->
			when (type) {
				selfType ->
					// TODO: Do we want this flexibility?
					value.let { value ->
						when (value) {
							is Int -> number(value).let { valueFn { it } }
							is Number -> valueFn
							else -> null
						}
					}
				else -> null
			}
		booleanType ->
			when (type) {
				selfType ->
					when (value) {
						is Boolean -> valueFn
						else -> null
					}
				else -> null
			}
		else -> null
	}
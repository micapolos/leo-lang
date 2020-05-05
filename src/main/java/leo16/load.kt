package leo16

import leo13.array
import leo13.linkOrNull
import leo13.push
import leo14.untyped.typed.loadClass

val loadedMap = mutableMapOf<Value, Value?>()

val Value.loadOrNull: Value?
	get() =
		loadOrNull("leo16.library.")

val Value.loadedOrNull: Value?
	get() =
		loadedMap.computeIfAbsent(this) { it.loadOrNull }

fun Value.loadOrNull(packagePrefix: String): Value? =
	nullIfThrowsException {
		wordStackOrNull!!.linkOrNull!!.let { wordLink ->
			packagePrefix
				.plus(
					wordLink.stack
						.push(wordLink.value.capitalize() + "Kt")
						.array
						.joinToString("."))
				.loadClass
				.getMethod("get" + wordLink.value.capitalize())
				.invoke(null) as Value
		}
	}
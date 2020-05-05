package leo16

import leo.base.The
import leo.base.the
import leo13.array
import leo13.linkOrNull
import leo13.push
import leo14.untyped.typed.loadClass

val loadedMap = mutableMapOf<Value, The<Value?>?>()

val Value.loadOrNull: Value?
	get() =
		loadOrNull("leo16.library.")

val Value.loadedOrNull: Value?
	get() {
		val theValue = loadedMap[this]
		return if (theValue != null) theValue.value
		else {
			loadedMap[this] = null.the
			val loadedValue = loadOrNull
			loadedMap[this] = loadedValue.the
			loadedValue
		}
	}

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
				//.also { loadingName(this).println }
				.invoke(null) as Value
		}
	}
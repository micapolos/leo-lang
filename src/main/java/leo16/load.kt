package leo16

import leo.base.The
import leo.base.the
import leo13.array
import leo13.linkOrNull
import leo13.push
import leo14.untyped.typed.loadClass
import leo15.dsl.*
import kotlin.collections.joinToString
import kotlin.collections.mutableMapOf
import kotlin.collections.set

val loadedMap = mutableMapOf<Value, The<Evaluated?>?>()

val Value.loadOrNull: Evaluated?
	get() =
		loadOrNull("leo16.library.")

val Value.loadedOrNull: Evaluated?
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

fun Value.loadOrNull(packagePrefix: String): Evaluated? =
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
				//.also { _loading(this).println }
				.invoke(null)
				.run {
					when (this) {
						is Evaluated -> this
						else -> compile_(this as F)
					}
				}
		}
	}
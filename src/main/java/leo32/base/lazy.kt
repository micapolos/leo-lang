package leo32.base

import leo.base.The
import leo.base.the

data class Lazy<T>(
	var theValueOrNullVar: The<T>?)

fun <T> newLazy() =
	Lazy<T>(null)

operator fun <T> Lazy<T>.invoke(fn: () -> T): T {
	val theValueOrNull = theValueOrNullVar
	return if (theValueOrNull != null) {
		theValueOrNull.value
	} else {
		val value = fn()
		theValueOrNullVar = the(value)
		return value
	}
}
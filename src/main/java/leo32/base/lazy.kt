package leo32.base

import leo.base.The
import leo.base.the

data class Lazy<T>(
	var theValueOrNullVar: The<T>?,
	val fn: () -> T)

fun <T> lazy(fn: () -> T) =
	Lazy(null, fn)

operator fun <T> Lazy<T>.invoke(): T {
	val theValueOrNull = theValueOrNullVar
	return if (theValueOrNull != null) {
		theValueOrNull.value
	} else {
		val value = fn()
		theValueOrNullVar = the(value)
		return value
	}
}
package leo16

import leo16.names.*

data class Fn(val pattern: Pattern, val nativeFn: (Value) -> Value) {
	override fun toString() = asValue.toString()
	val asValue = pattern.asValue.plus(_does(nativeFn.nativeString()))

	// TODO: Remove _given, and pass value directly
	fun apply(arg: Value): Value? =
		pattern.matchOrNull(arg)?.value?.let { value ->
			try {
				nativeFn(_given(value).value)
			} catch (throwable: Throwable) {
				_error(throwable.nativeValue).value
			}
		}
}

fun Pattern.fn(nativeFn: (Value) -> Value) = Fn(this, nativeFn)

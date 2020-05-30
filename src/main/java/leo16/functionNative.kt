package leo16

import leo.base.notNullIf
import leo16.names.*

data class Fn(val patternValue: Value, val nativeFn: (Value) -> Value) {
	override fun toString() = asValue.toString()
	val asValue = patternValue.plus(_does(nativeFn.nativeString()))

	// TODO: Remove _given, and pass value directly
	fun apply(arg: Value): Value? =
		notNullIf(patternValue.matches(arg)) {
			try {
				nativeFn(_given(arg).value)
			} catch (throwable: Throwable) {
				_error(throwable.nativeValue).value
			}
		}
}

fun Value.fn(nativeFn: (Value) -> Value) = Fn(this, nativeFn)

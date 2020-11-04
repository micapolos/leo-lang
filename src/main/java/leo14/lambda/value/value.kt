package leo14.lambda.value

import leo14.ScriptLine
import leo14.lambda.Term
import leo14.lambda.term
import leo14.lineTo
import leo14.nativeScriptLine
import leo14.script

sealed class Value<out T> {
	override fun toString() = scriptLine { nativeScriptLine }.toString()
}

data class NativeValue<T>(val native: T) : Value<T>() {
	override fun toString() = super.toString()
}

data class FunctionValue<T>(val function: Function<T>) : Value<T>() {
	override fun toString() = super.toString()
}

fun <T> Value<T>.scriptLine(nativeScriptLine: T.() -> ScriptLine): ScriptLine =
	"value" lineTo script(
		when (this) {
			is NativeValue -> "native" lineTo script(native.nativeScriptLine())
			is FunctionValue -> function.scriptLine(nativeScriptLine)
		}
	)

fun <T> value(native: T): Value<T> = NativeValue(native)
fun <T> value(function: Function<T>): Value<T> = FunctionValue(function)

val <T> Value<T>.native: T get() = (this as NativeValue).native
val <T> Value<T>.function: Function<T> get() = (this as FunctionValue).function

fun <T> Value<T>.apply(value: Value<T>, nativeApply: NativeApply<T>): Value<T> =
	when (this) {
		is NativeValue -> native.nativeApply(value)
		is FunctionValue -> function.apply(value, nativeApply)
	}

val <T> Value<T>.term: Term<T>
	get() =
		when (this) {
			is NativeValue -> term(native)
			is FunctionValue -> function.term
		}

fun Any.anyApply(value: Value<Any>): Value<Any> =
	value((this as (Any) -> Any).invoke(value.native))
